package com.example.orderfood

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchBarActivity : AppCompatActivity() {
    lateinit var rv_food_items : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rv_food_items = findViewById(R.id.rv_food_items)

//        sharedPreferences = getSharedPreferences("search_history", MODE_PRIVATE)

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)  // Remove ActionBar back arrow
//        supportActionBar?.setHomeButtonEnabled(false)
//
//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black))
//        loadSearchHistory()
    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = "Search dishes..."

        // ====== Customize background ======
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 10f * resources.displayMetrics.density  // 10dp
            setColor(Color.WHITE) // background color
        }

        searchView?.background = drawable
        searchView?.elevation = 10f * resources.displayMetrics.density  // elevation 10dp

        // ====== Customize text ======
        val searchAutoCompleteField = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
        searchAutoCompleteField.isAccessible = true
        val searchAutoComplete = searchAutoCompleteField.get(searchView) as? EditText
        searchAutoComplete?.setTextColor(Color.BLACK)
        searchAutoComplete?.setHintTextColor(Color.GRAY)

        // ====== Tint icons with primary color ======
        val primaryColor = ContextCompat.getColor(this, R.color.primaryColor)

        val backButton = searchView?.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        backButton?.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN)

        val closeButton = searchView?.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN)

        val voiceButton = searchView?.findViewById<ImageView>(androidx.appcompat.R.id.search_voice_btn)
        voiceButton?.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN)

        // ====== SearchView listeners ======
*//*        searchAutoComplete?.setOnItemClickListener { parent, view, position, id ->
            val query = parent.getItemAtPosition(position) as String
            searchView.setQuery(query, true)
        }*//*

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submit
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search text change
                return false
            }
        })

        searchItem?.expandActionView()
        searchView?.requestFocus()

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean = true
            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                onBackPressed()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }*/

   /* private fun loadSearchHistory() {
        val searchHistory = sharedPreferences.getStringSet("history", setOf())?.toMutableList()
        searchHistoryAdapter = ArrayAdapter(this, R.layout.dropdown_item, searchHistory ?: listOf())
    }*/

    /*private fun saveSearchQuery(query: String) {
        val maxHistorySize = 10 // Maximum number of search queries to keep
        val searchHistory = sharedPreferences.getStringSet("history", mutableSetOf())?.toMutableSet()

        if (searchHistory != null) {
            // Add the new query if it doesn't already exist
            if (searchHistory.contains(query)) {
                searchHistory.remove(query)
            }
            searchHistory.add(query)

            // Trim the history if it exceeds the maximum size
            if (searchHistory.size > maxHistorySize) {
                val excessCount = searchHistory.size - maxHistorySize
                val iterator = searchHistory.iterator()
                repeat(excessCount) {
                    if (iterator.hasNext()) iterator.next().let { iterator.remove() }
                }
            }

            // Save the updated history
            sharedPreferences.edit().putStringSet("history", searchHistory).apply()
            loadSearchHistory() // Reload the search history to update the dropdown
        }
    }*/


    /*fun searchExams(query: String) {
        var exam_data : CreateQuestions
        val db = FirebaseFirestore.getInstance()
        db.collection("Exams")
            .whereEqualTo("exam_id", query)
            .get()
            .addOnSuccessListener { documents ->
                val examList = mutableListOf<CreateQuestions>()
                if (!documents.isEmpty) {
                    // Handle no results found
                    for (document in documents) {
                        exam_data = document.toObject(CreateQuestions::class.java)
                        examList.add(exam_data)
                    }
                    saveSearchQuery(query)
                    db.collection("History").document(user).collection("HistoryDetails").whereEqualTo("exam_id",query)
                        .addSnapshotListener { documents, error ->
                            if (error != null) {
                                Log.w("Firestore", "Listen failed.", error)
                                return@addSnapshotListener
                            }

                            if (documents != null && !documents.isEmpty) {
                                examDataList.clear()  // Clear the list before adding updated data
                                for (document in documents) {
                                    val answerKey = document.toObject(AnswerKey::class.java)
                                    examDataList.add(answerKey)
                                }
                            } else {
                                Log.d("Firestore", "No data found")
                            }
                        }
                    // Initialize the adapter with a list of exams
                    val examDetailsAdapter = SearchFoodAdapter(this, examList,examDataList)
                    rv_food_items.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    rv_food_items.adapter = examDetailsAdapter
                }else{
                    val examDetailsAdapter = SearchFoodAdapter(this, examList,examDataList)
                    rv_food_items.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    rv_food_items.adapter = examDetailsAdapter
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching exams", Toast.LENGTH_SHORT).show()
            }
    }*/
}