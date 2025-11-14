package com.example.orderfood.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.interfaces.OnFoodItemClickListener
import com.example.orderfood.interfaces.OnOrderItemListener
import com.example.orderfood.interfaces.OnOrderRequestListener
import com.example.orderfood.model.Category
import com.example.orderfood.model.OrderDetails

class PendingOrderListAdapter(private val context: Context, private val listener: OnOrderRequestListener, private val list: List<OrderDetails>) : RecyclerView.Adapter<PendingOrderListAdapter.ViewHolder>() {
    private var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_order_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tv_food_name.text = item.items[0].name
        holder.tv_total_price.text = item.finalAmount.toString()
        holder.tv_order_status.text = item.orderStatus
        Glide.with(context)
            .load(item.items.get(0).imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.iv_food_image)
        holder.ll_order_item.setOnClickListener {
            listener.onOrderRequestClicked()
        }
        if(!item.orderStatus.equals("Delivered")){
            holder.tv_order_status.setTextColor(Color.parseColor("#FD8200"))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iv_food_image : ImageView
        var tv_food_name : TextView
        var tv_total_price : TextView
        var tv_order_status : TextView
        var ll_order_item : LinearLayout
        init {
            iv_food_image = view.findViewById(R.id.iv_food_image)
            tv_food_name = view.findViewById(R.id.tv_food_name)
            tv_total_price = view.findViewById(R.id.tv_total_price)
            tv_order_status = view.findViewById(R.id.tv_order_status)
            ll_order_item = view.findViewById(R.id.ll_order_item)
        }
    }
}