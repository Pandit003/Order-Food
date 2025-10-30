package com.example.orderfood.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItems(
    @PrimaryKey
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var categoryId: String = "",
    var price: Double = 0.0,
    var discountPercent: Double = 0.0,
    val finalPrice: Double = 0.0,
    var isAvailable: Boolean = true,
    var rating: Float = 0f,
    var totalRatings: Int = 0,
    var quantity: Int = 0
)

