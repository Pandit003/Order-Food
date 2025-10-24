package com.example.orderfood.model

data class FoodItem(
    val id: String = "",                     // Unique ID
    val name: String = "",                   // Food name
    val description: String = "",            // Short description
    val imageUrl: String = "",               // Image URL or URI
    val categoryId: String = "",             // Link to category
    val price: Double = 0.0,                 // Base price
    val discountPercent: Double = 0.0,       // Optional discount
    val isAvailable: Boolean = true,         // Availability flag
    val rating: Float = 0f,                  // Average rating
    val totalRatings: Int = 0,               // Total number of ratings
    val tags: List<String> = emptyList(),    // Search/filter keywords
    val addons: List<AddOn> = emptyList()    // Extra items like cheese, coke, etc.
)
data class AddOn(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0
)
data class Category(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = ""
)
