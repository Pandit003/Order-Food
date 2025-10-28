package com.example.orderfood.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.AddToCartAdapter
import com.example.orderfood.adapter.SearchFoodAdapter

class AddToCartActivity : AppCompatActivity() {
    private lateinit var rv_addtocart: RecyclerView
    private lateinit var addToCartAdapter: AddToCartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_to_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rv_addtocart = findViewById(R.id.rv_addtocart)
        rv_addtocart.layoutManager = LinearLayoutManager(this)
        addToCartAdapter  = AddToCartAdapter()
        rv_addtocart.adapter = addToCartAdapter
    }
}