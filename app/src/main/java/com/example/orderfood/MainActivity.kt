package com.example.orderfood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.adapter.FoodCategoriesAdapter
import com.example.orderfood.adapter.SearchFoodAdapter
import com.example.orderfood.model.Category
import com.example.orderfood.model.FoodItem

class MainActivity : AppCompatActivity() {
    private val searchSuggestions = listOf("'Soup'", "'Roti'", "'Biryani'")
    private var hintIndex = 0
    private lateinit var etSearch: EditText
    private lateinit var rv_catOption: RecyclerView
    private lateinit var rv_allfood: RecyclerView
    private val hintHandler = Handler(Looper.getMainLooper())
    private lateinit var foodCategoriesAdapter: FoodCategoriesAdapter
    private lateinit var searchFoodAdapter: SearchFoodAdapter
    private val hintRunnable = object : Runnable {
        override fun run() {
            val animOut = AnimationUtils.loadAnimation(this@MainActivity, R.anim.hint_up)
            val animIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.hint_in)

            animOut.setAnimationListener(object :
                android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation) {}
                override fun onAnimationEnd(animation: android.view.animation.Animation) {
                    // Change hint after moving up
                    etSearch.hint = "Search ${searchSuggestions[hintIndex]}"
                    hintIndex = (hintIndex + 1) % searchSuggestions.size
                    etSearch.startAnimation(animIn)
                }

                override fun onAnimationRepeat(animation: android.view.animation.Animation) {}
            })

            etSearch.startAnimation(animOut)
            hintHandler.postDelayed(this, 4000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etSearch = findViewById(R.id.et_search)
        val iv_search : ImageView = findViewById(R.id.iv_search)
        val iv_back : ImageView = findViewById(R.id.iv_back)
        iv_search.isVisible = true
        iv_back.isVisible = false
        etSearch.isFocusable = false
        etSearch.setOnClickListener{
            val intent = Intent(this, SearchBarActivity::class.java)
            startActivity(intent)

        }

        hintHandler.post(hintRunnable)

        val categories = listOf(
            Category("", "All",R.drawable.all_food),
            Category("", "Biryani",R.drawable.biryani_img),
            Category("", "Noodles",R.drawable.noodles),
            Category("", "Soup",R.drawable.soup),
            Category("", "Roti",R.drawable.roti_1)
        )
        val foodItems = loadFoodItemsFromAssets(this)
        rv_catOption = findViewById(R.id.rv_catOption)
        rv_catOption.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        foodCategoriesAdapter = FoodCategoriesAdapter(this,categories)  // or your data list
        rv_catOption.adapter = foodCategoriesAdapter

        rv_allfood = findViewById(R.id.rv_Allfood)
        rv_allfood.layoutManager = LinearLayoutManager(this)
        searchFoodAdapter  = SearchFoodAdapter(this,foodItems)  // or your data list
        rv_allfood.adapter = searchFoodAdapter
    }
    private fun getDummyList(): List<String> = listOf(
        "Pizza", "Burger", "Pasta", "Sandwich", "Momos", "Noodles", "Biryani", "Ice Cream"
    )
    override fun onDestroy() {
        super.onDestroy()
        hintHandler.removeCallbacks(hintRunnable) // stop updates when activity is destroyed
    }
    fun loadFoodItemsFromAssets(context: Context): List<FoodItem> {
        val json = context.assets.open("food_items.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<FoodItem>>() {}.type
        return Gson().fromJson(json, listType)
    }
    /*override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }*/
}