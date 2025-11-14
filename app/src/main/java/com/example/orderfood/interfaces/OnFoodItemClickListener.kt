package com.example.orderfood.interfaces

import com.example.orderfood.model.OrderDetails
import com.example.orderfood.model.personalDetail

interface OnFoodItemClickListener {
    fun onFoodItemClicked(name: String)
}

interface OnSelectFoodClickListener {
    fun onSelectedClicked(pos: Int)

}
interface OnAddCartItemListener {
    fun onCartItemClicked()

}
interface OnDeleteCartItemListener {
    fun onDeleteCartItemClicked(id: Int)
}
interface OnOrderItemListener {
    fun onOrderItemClicked(id: String,orderData : OrderDetails)
}
interface OnPersonSelectedListener {
    fun onPersonSelectedClicked(userData : personalDetail)
}
interface OnOrderRequestListener {
    fun onOrderRequestClicked()
}

