package com.example.orderfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.interfaces.OnFoodItemClickListener
import com.example.orderfood.interfaces.OnSelectFoodClickListener
import com.example.orderfood.model.FoodItem

class AddToCartAdapter() : RecyclerView.Adapter<AddToCartAdapter.ViewHolder>() {
/*    private var filteredItems = allItems.toMutableList()
    private val context = listener as Context*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layou_add_to_cart,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*val item = filteredItems[position]
        holder.tv_name.text = item.name
        holder.tv_rating.text = item.rating.toString()
        holder.tv_rated_by.text = "By "+item.totalRatings.toString()+"+"
        holder.tv_desc.text = item.description
        holder.tv_price.text = "Only ₹ "+item.price.toString()
        Glide.with(context)
            .load(item.imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.iv_food_img)
        holder.iv_food_img.setOnClickListener {
            listener.onSelectedClicked(position)
        }*/
    }

    override fun getItemCount(): Int {
        return 2
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        /*var tv_name : TextView
        var tv_desc : TextView
        var tv_rating : TextView
        var tv_rated_by : TextView
        var tv_price : TextView
        var iv_food_img : ImageView
        var red_line : View
        init {
            tv_name = view.findViewById(R.id.tv_name)
            tv_desc = view.findViewById(R.id.tv_desc)
            tv_rating = view.findViewById(R.id.tv_rating)
            tv_rated_by = view.findViewById(R.id.tv_rated_by)
            tv_price = view.findViewById(R.id.tv_price)
            iv_food_img = view.findViewById(R.id.iv_food_img)
            red_line = view.findViewById(R.id.red_line)
        }*/
    }
    /*fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            allItems.toMutableList()
        } else {
            allItems.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }*/
}