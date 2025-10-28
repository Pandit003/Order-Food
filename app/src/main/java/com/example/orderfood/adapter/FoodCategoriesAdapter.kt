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

class FoodCategoriesAdapter(private val listener: OnFoodItemClickListener, private val list: List<Category>) : RecyclerView.Adapter<FoodCategoriesAdapter.ViewHolder>() {
    private var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_categories_food,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.iv_img.setImageResource(item.imageUrl)
        holder.tv_name.text = item.name
        holder.red_line.visibility = View.GONE
        holder.red_line.isVisible = (selectedPosition == position)

        holder.ll_option.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            if (previousPosition != RecyclerView.NO_POSITION)
                notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            listener.onFoodItemClicked(item.name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iv_img : ImageView
        var tv_name : TextView
        var red_line : View
        var ll_option : LinearLayout
        init {
            iv_img = view.findViewById(R.id.iv_img)
            tv_name = view.findViewById(R.id.tv_name)
            red_line = view.findViewById(R.id.red_line)
            ll_option = view.findViewById(R.id.ll_option)
        }
    }
}