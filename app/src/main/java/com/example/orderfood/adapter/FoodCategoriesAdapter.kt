package com.example.orderfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.model.Category

class FoodCategoriesAdapter(var context: Context, private val list: List<Category>) : RecyclerView.Adapter<FoodCategoriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_categories_food,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.iv_img.setImageResource(item.imageUrl)
        holder.tv_name.text = item.name
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iv_img : ImageView
        var tv_name : TextView
        init {
            iv_img = view.findViewById(R.id.iv_img)
            tv_name = view.findViewById(R.id.tv_name)
        }
    }
}