package com.example.orderfood.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val categoryId: String = "",
    val price: Double = 0.0,
    val discountPercent: Double = 0.0,
    val finalPrice: Double = 0.0,
    val isAvailable: Boolean = true,
    val rating: Float = 0f,
    val totalRatings: Int = 0,
    val quantity: Int = 1,
    val tags: List<String> = emptyList(),
    val addons: List<AddOn> = emptyList()
) : Parcelable

@Parcelize
data class AddOn(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0
): Parcelable

@Parcelize
data class Category(
    val id: String = "",
    val name: String = "",
    val imageUrl: Int
): Parcelable
