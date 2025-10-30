package com.example.orderfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.interfaces.OnFoodItemClickListener
import com.example.orderfood.model.Category
import com.example.orderfood.model.OrderItem

class OrderItemAdapter(private val list : List<OrderItem>) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_order_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tv_order_food_name.text = item.name
        holder.tv_order_food_price.text = "₹${item.totalPrice.toString()}"
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var tv_order_food_name : TextView
        var tv_order_food_price : TextView
        init {
            tv_order_food_name = view.findViewById(R.id.tv_order_food_name)
            tv_order_food_price = view.findViewById(R.id.tv_order_food_price)
        }
    }
}