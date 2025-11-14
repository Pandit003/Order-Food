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
import com.example.orderfood.interfaces.OnPersonSelectedListener
import com.example.orderfood.interfaces.OnSelectFoodClickListener
import com.example.orderfood.model.FoodItem
import com.example.orderfood.model.personalDetail

class SearchPhoneNoAdapter(private val listener: OnPersonSelectedListener, private var userlist: List<personalDetail>) : RecyclerView.Adapter<SearchPhoneNoAdapter.ViewHolder>() {
    private val context = listener as Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_phone_details,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userlist[position]
        holder.tv_name.text = item.name
        holder.tv_phone.text = item.phone
        holder.ll_user_info.setOnClickListener {
            listener.onPersonSelectedClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var tv_name : TextView
        var tv_phone : TextView
        var ll_user_info : LinearLayout
        init {
            tv_name = view.findViewById(R.id.tv_name)
            tv_phone = view.findViewById(R.id.tv_phone)
            ll_user_info = view.findViewById(R.id.ll_user_info)
        }
    }
}