package com.example.orderfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R

class SearchFoodAdapter(var context: Context, private val allItems: List<String>) : RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>() {
    private var filteredItems = allItems.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_food_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.user_id.text = filteredItems[position]
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var host_date : TextView
        var full_mark : TextView
        var user_id : TextView
        var subject_name : TextView
        var start_exam_btn : Button
        init {
            host_date = view.findViewById(R.id.host_date)
            full_mark = view.findViewById(R.id.full_mark)
            user_id = view.findViewById(R.id.user_id)
            subject_name = view.findViewById(R.id.subject_name)
            start_exam_btn = view.findViewById(R.id.start_exam_btn)
        }
    }
    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            allItems.toMutableList()
        } else {
            allItems.filter { it.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
}