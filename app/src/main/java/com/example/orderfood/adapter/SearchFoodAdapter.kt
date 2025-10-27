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
import com.example.orderfood.model.FoodItem

class SearchFoodAdapter(var context: Context, private var allItems: List<FoodItem>) : RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>() {
    private var filteredItems = allItems.toMutableList()
    private var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_food_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.text = allItems[position].name
        holder.tv_rating.text = allItems[position].rating.toString()
        holder.tv_rated_by.text = "By "+allItems[position].totalRatings.toString()+"+"
        holder.tv_desc.text = allItems[position].description
        holder.tv_price.text = "Only ₹ "+allItems[position].price.toString()

        Glide.with(context)
            .load(allItems[position].imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.iv_food_img)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var tv_name : TextView
        var tv_desc : TextView
        var tv_rating : TextView
        var tv_rated_by : TextView
        var tv_price : TextView
        var iv_food_img : ImageView
        var red_line : View
        var ll_option : LinearLayout
        init {
            tv_name = view.findViewById(R.id.tv_name)
            tv_desc = view.findViewById(R.id.tv_desc)
            tv_rating = view.findViewById(R.id.tv_rating)
            tv_rated_by = view.findViewById(R.id.tv_rated_by)
            tv_price = view.findViewById(R.id.tv_price)
            iv_food_img = view.findViewById(R.id.iv_food_img)
            red_line = view.findViewById(R.id.red_line)
            ll_option = view.findViewById(R.id.ll_option)
        }
    }
    fun filter(query: String) {
        allItems = if (query.isEmpty()) {
            allItems.toMutableList()
        } else {
            allItems.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
}