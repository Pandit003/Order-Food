package com.example.orderfood.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.widgets.FoodDetailBottomSheet
import com.example.orderfood.R
import com.example.orderfood.adapter.SearchFoodAdapter
import com.example.orderfood.interfaces.OnSelectFoodClickListener
import com.example.orderfood.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchBarActivity : AppCompatActivity(), OnSelectFoodClickListener {
    private lateinit var searchFoodAdapter: SearchFoodAdapter
    private lateinit var recyclerView: RecyclerView
    var foodItems : List<FoodItem> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
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
        recyclerView = findViewById(R.id.rv_food_items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchFoodAdapter  = SearchFoodAdapter(this,foodItems)  // or your data list
        recyclerView.adapter = searchFoodAdapter
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

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = !s.isNullOrEmpty()
                searchFoodAdapter.filter(s.toString())
                ivClear.visibility = if (hasText) View.VISIBLE else View.GONE
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

    override fun onSelectedClicked(pos: Int) {
        val sheet = FoodDetailBottomSheet(
            foodItems[pos]
        )
        sheet.show(supportFragmentManager, "FoodDetailBottomSheet")
    }
}