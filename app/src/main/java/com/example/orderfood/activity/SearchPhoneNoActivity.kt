package com.example.orderfood.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.SearchPhoneNoAdapter
import com.example.orderfood.interfaces.OnPersonSelectedListener
import com.example.orderfood.model.FoodItem
import com.example.orderfood.model.personalDetail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchPhoneNoActivity : AppCompatActivity(), OnPersonSelectedListener {
    private lateinit var searchPhoneNoAdapter : SearchPhoneNoAdapter
    private lateinit var recyclerView: RecyclerView
    var foodItems : List<FoodItem> = listOf()
    var UserList = mutableListOf<personalDetail>()
    private var searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    lateinit var db : FirebaseFirestore
    lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_phone_no)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val root: View = findViewById(R.id.search_root)
        val etSearch: EditText = findViewById(R.id.et_search)
        val ivClear: ImageView = findViewById(R.id.iv_clear)
        val ivMic: ImageView = findViewById(R.id.iv_mic)
        val ivBack: ImageView = findViewById(R.id.iv_back)
        foodItems = loadFoodItemsFromAssets(this)
        recyclerView = findViewById(R.id.rv_phone_nos)
        progress = ProgressDialog(this)
        progress.setCancelable(false)
        progress.setMessage("Loading...")
        db = FirebaseFirestore.getInstance()
        etSearch.requestFocus()
        root.setOnClickListener {
            etSearch.requestFocus()
            showKeyboard(etSearch)
        }

        ivBack.setOnClickListener {
            if (this is Activity) finish()
            else {
                // or call your nav controller popBackStack()
            }
        }

        ivMic.setOnClickListener {
            // Start voice input (example using RecognizerIntent)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
            }
            speechLauncher.launch(intent)
        }

        ivClear.setOnClickListener {
            etSearch.text?.clear()
            ivClear.visibility = View.GONE
        }
        etSearch.inputType = InputType.TYPE_CLASS_PHONE
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = !s.isNullOrEmpty()
                ivClear.visibility = if (hasText) View.VISIBLE else View.GONE
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                searchRunnable = Runnable {
                    progress.show()
                    getUser(s.toString())
                }

                searchHandler.postDelayed(searchRunnable!!, 2000)

            }
            override fun afterTextChanged(s: Editable?) {}
        })
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = etSearch.text.toString().trim()
                hideKeyboard(etSearch)
                true
            } else false
        }
    }
    fun getUser(query: String) {
        if (query.isBlank()) return

        UserList.clear()

        db.collection("personalDetails")
            .whereEqualTo("phone", query.trim())
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val user = personalDetail(
                            name = document.getString("name") ?: "",
                            phone = document.getString("phone") ?: "",
                            area = document.getString("area") ?: "",
                            landmark = document.getString("landmark") ?: "",
                            house_number = document.getString("house_no") ?: ""
                        )
                        UserList.add(user)
                    }

                    // Set adapter only once, after all data is added
                    if (::searchPhoneNoAdapter.isInitialized) {
                        searchPhoneNoAdapter.notifyDataSetChanged()
                    } else {
                        recyclerView.layoutManager = LinearLayoutManager(this@SearchPhoneNoActivity)
                        searchPhoneNoAdapter = SearchPhoneNoAdapter(this@SearchPhoneNoActivity, UserList)
                        recyclerView.adapter = searchPhoneNoAdapter
                    }

                } else {
                    Log.d("Firestore", "No users found for phone: $query")
                    Toast.makeText(this, "No record found.", Toast.LENGTH_SHORT).show()
                }
                progress.dismiss()
            }
            .addOnFailureListener { exception ->
                progress.dismiss()
                Log.e("FirestoreError", "Error fetching data", exception)
                Toast.makeText(this, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(view: View) {
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private val speechLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val matches = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                findViewById<EditText>(R.id.et_search).setText(matches[0])
            }
        }
    }
    fun loadFoodItemsFromAssets(context: Context): List<FoodItem> {
        val json = context.assets.open("food_items.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<FoodItem>>() {}.type
        return Gson().fromJson(json, listType)
    }

    override fun onPersonSelectedClicked(userData: personalDetail) {
        Log.d("TAG", "onPersonSelectedClicked: $userData")
        var intent = Intent(this, AdminUserInfoActivity::class.java)
        intent.putExtra("UserInfo", userData.toString())
        startActivity(intent)
    }


}