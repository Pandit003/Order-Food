package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.orderfood.R

class AdminActivity : AppCompatActivity() {
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
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
            val intent = Intent(this, SearchPhoneNoActivity::class.java)
            startActivity(intent)
        }
    }
}