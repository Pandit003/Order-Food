package com.example.orderfood.interfaces

import com.example.orderfood.model.OrderDetails

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

