package com.example.orderfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetails(
    val orderId: String = "",
    val userId: String = "",
    val restaurantName: String = "",
    val restaurantAddress: String? = null,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val discountAmount: Double = 0.0,
    val deliveryCharge: Double = 0.0,
    val finalAmount: Double = 0.0,
    val orderDate: String = "",
    val deliveryTime: String? = null,
    val estimatedDeliveryMinutes: Int? = null,
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val orderStatus: String = "",
    val deliveryAddress: String = "",
    val deliveryPersonName: String? = null,
    val deliveryPersonPhone: String? = null,
    val specialInstructions: String? = null,
    val rating: Float? = null,
    val feedback: String? = null,
    val timestamp: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
) : Parcelable

@Parcelize
data class OrderItem(
    val itemId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val pricePerItem: Double = 0.0,
    val imageUrl: String? = null,
    val totalPrice: Double = 0.0
) : Parcelable


