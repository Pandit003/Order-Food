package com.example.orderfood.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.interfaces.OnAddCartItemListener
import com.example.orderfood.interfaces.OnFoodItemClickListener
import com.example.orderfood.interfaces.OnSelectFoodClickListener
import com.example.orderfood.localDatabase.AppDatabase
import com.example.orderfood.model.CartItems
import com.example.orderfood.model.FoodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToCartAdapter(private val listener: OnAddCartItemListener, private var cartFood : List<CartItems>) : RecyclerView.Adapter<AddToCartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layou_add_to_cart,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.text = cartFood.get(position).name
        holder.tv_count.text = cartFood.get(position).quantity.toString()
        holder.tv_final_price.text = "₹"+cartFood.get(position).finalPrice.toString()
        holder.myRatingBar.rating = cartFood.get(position).rating
        holder.tv_rated_by.text = "("+cartFood.get(position).totalRatings.toString()+")"
        holder.tv_price.apply {
            text = "₹${String.format("%.2f", cartFood.get(position).price)}"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        holder.tv_discount.text = " ↓"+cartFood.get(position).discountPercent.toInt().toString()+" % "
        Glide.with(listener as Context)
            .load(cartFood.get(position).imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.iv_food_image)
        holder.iv_minus.setOnClickListener {
            var count = holder.tv_count.text.toString().toInt()
            if(count>1){
                count--
                holder.tv_count.text = count.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(listener as Context)
                    db.messageDao().updateQuantity(cartFood[position].id, count)
                    withContext(Dispatchers.Main) {
                        listener.onCartItemClicked()
                    }
                }
            }
        }
        holder.iv_plus.setOnClickListener {
            var count = holder.tv_count.text.toString().toInt()
            count++
            holder.tv_count.text = count.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(listener as Context)
                db.messageDao().updateQuantity(cartFood[position].id, count)
                withContext(Dispatchers.Main) {
                    listener.onCartItemClicked()
                }
            }
        }
        holder.tv_remove.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(listener as Context)
                db.messageDao().deleteById(cartFood[position].id)
                withContext(Dispatchers.Main) {
                    listener.onCartItemClicked()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cartFood.size
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iv_minus : ImageView
        var iv_plus : ImageView
        var iv_food_image : ImageView
        var tv_count : TextView
        var tv_name : TextView
        var tv_price : TextView
        var tv_discount : TextView
        var tv_final_price : TextView
        var tv_rated_by : TextView
        var tv_remove : TextView
        var tv_buy : TextView
        var myRatingBar : RatingBar
        init {
            iv_minus = view.findViewById(R.id.iv_minus)
            iv_plus = view.findViewById(R.id.iv_plus)
            iv_food_image = view.findViewById(R.id.iv_food_image)
            tv_count = view.findViewById(R.id.tv_count)
            tv_name = view.findViewById(R.id.tv_name)
            tv_price = view.findViewById(R.id.tv_price)
            tv_discount = view.findViewById(R.id.tv_discount)
            tv_final_price = view.findViewById(R.id.tv_final_price)
            tv_rated_by = view.findViewById(R.id.tv_rated_by)
            tv_remove = view.findViewById(R.id.tv_remove)
            tv_buy = view.findViewById(R.id.tv_buy)
            myRatingBar = view.findViewById(R.id.myRatingBar)
        }
    }

    fun updateCartItems(newCartFood: List<CartItems>) {
        cartFood = newCartFood
        notifyDataSetChanged()
    }

}