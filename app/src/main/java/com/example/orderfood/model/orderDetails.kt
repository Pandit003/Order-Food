package com.example.orderfood.model

data class OrderDetails(
    val orderId: String,
    val userId: String,
    val restaurantName: String,
    val restaurantAddress: String?,

    val items: List<OrderItem>,

    val totalAmount: Double,
    val discountAmount: Double = 0.0,
    val deliveryCharge: Double = 0.0,
    val finalAmount: Double,

    val orderDate: String,
    val deliveryTime: String?,
    val estimatedDeliveryMinutes: Int?,

    val paymentMethod: String,
    val paymentStatus: String,

    val orderStatus: String,

    val deliveryAddress: String,
    val deliveryPersonName: String?,
    val deliveryPersonPhone: String?,

    val specialInstructions: String? = null,
    val rating: Float? = null,
    val feedback: String? = null
)
data class OrderItem(
    val itemId: String,
    val name: String,
    val quantity: Int,
    val pricePerItem: Double,
    val imageUrl: String? = null,
    val totalPrice: Double
)

