package com.example.orderfood.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.model.FoodItem

class SelectedItemAdapter(private val context: Context , private val fooditemList : List<FoodItem>) : RecyclerView.Adapter<SelectedItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_selected_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedItemAdapter.ViewHolder, position: Int) {
        holder.tv_name.text = fooditemList.get(position).name
        holder.tv_final_price.text = "₹"+fooditemList.get(position).finalPrice.toString()
        holder.myRatingBar.rating = fooditemList.get(position).rating
        holder.tv_rated_by.text = "("+fooditemList.get(position).totalRatings.toString()+")"
        holder.tv_price.apply {
            text = "₹${String.format("%.2f", fooditemList.get(position).price)}"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        holder.tv_discount.text = " ↓"+fooditemList.get(position).discountPercent.toInt().toString()+" % "
        Glide.with(context)
            .load(fooditemList.get(position).imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.iv_food_image)
    }

    override fun getItemCount(): Int {
        return fooditemList.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iv_food_image : ImageView
        var tv_name : TextView
        var tv_price : TextView
        var tv_discount : TextView
        var tv_final_price : TextView
        var tv_rated_by : TextView
        var myRatingBar : RatingBar
        init {
            iv_food_image = view.findViewById(R.id.iv_food_image)
            tv_name = view.findViewById(R.id.tv_name)
            tv_price = view.findViewById(R.id.tv_price)
            tv_discount = view.findViewById(R.id.tv_discount)
            tv_final_price = view.findViewById(R.id.tv_final_price)
            tv_rated_by = view.findViewById(R.id.tv_rated_by)
            myRatingBar = view.findViewById(R.id.myRatingBar)
        }
    }
}
