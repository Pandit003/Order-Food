package com.example.orderfood.interfaces

interface OnFoodItemClickListener {
    fun onFoodItemClicked(name: String)
}

interface OnSelectFoodClickListener {
    fun onSelectedClicked(pos: Int)
}

