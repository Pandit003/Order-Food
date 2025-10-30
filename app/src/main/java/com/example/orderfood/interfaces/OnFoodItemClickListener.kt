package com.example.orderfood.interfaces

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
    fun onOrderItemClicked(id: String)
}

