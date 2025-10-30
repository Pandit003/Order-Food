package com.example.orderfood.activity

import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.localDatabase.AppDatabase
import com.example.orderfood.R
import com.example.orderfood.adapter.AddToCartAdapter
import com.example.orderfood.interfaces.OnAddCartItemListener
import com.example.orderfood.interfaces.OnDeleteCartItemListener
import com.example.orderfood.model.CartItems
import kotlinx.coroutines.launch

class AddToCartActivity : AppCompatActivity(),OnAddCartItemListener, OnDeleteCartItemListener {
    private lateinit var rv_addtocart: RecyclerView
    private lateinit var addToCartAdapter: AddToCartAdapter
    private lateinit var tv_total_price: TextView
    private lateinit var tv_discount_price: TextView
    private lateinit var tv_delivery_charges: TextView
    private lateinit var tv_final_price: TextView
    private lateinit var tv_total_amount: TextView
    private lateinit var tv_amount: TextView
    private lateinit var tv_savings: TextView
    private lateinit var tv_after_dis: TextView
    private lateinit var tv_empty_cart: TextView
    private lateinit var ll_place_order: LinearLayout
    private lateinit var ll_price_details: LinearLayout
    var cartFood: MutableList<CartItems> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_to_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        tv_total_price = findViewById(R.id.tv_total_price)
        tv_discount_price = findViewById(R.id.tv_discount_price)
        tv_delivery_charges = findViewById(R.id.tv_delivery_charges)
        tv_final_price = findViewById(R.id.tv_final_price)
        tv_total_amount = findViewById(R.id.tv_total_amount)
        tv_amount = findViewById(R.id.tv_amount)
        tv_savings = findViewById(R.id.tv_savings)
        tv_after_dis = findViewById(R.id.tv_after_dis)
        ll_place_order = findViewById(R.id.ll_place_order)
        ll_price_details = findViewById(R.id.ll_price_details)
        tv_empty_cart = findViewById(R.id.tv_empty_cart)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Cart Items"
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            cartFood = db.messageDao().getAllMessages().toMutableList()

            rv_addtocart = findViewById(R.id.rv_addtocart)
            rv_addtocart.layoutManager = LinearLayoutManager(this@AddToCartActivity)
            addToCartAdapter = AddToCartAdapter(this@AddToCartActivity, cartFood)
            rv_addtocart.adapter = addToCartAdapter

            if(cartFood.isEmpty()) {
                ll_place_order.visibility = android.view.View.GONE
                ll_price_details.visibility = android.view.View.GONE
                rv_addtocart.visibility = android.view.View.GONE
                tv_empty_cart.visibility = android.view.View.VISIBLE
            }
            setData()
        }

    }

    private fun setData() {
        var totalPrice = 0.0
        var totalDiscount = 0.0

        for (item in cartFood) {
            val itemTotal = item.price * item.quantity
            val itemDiscount = (item.price * item.discountPercent / 100) * item.quantity

            totalPrice += itemTotal
            totalDiscount += itemDiscount
        }

        val deliveryCharge = if (totalPrice - totalDiscount >= 500) 0.0 else 50.0
        val afterDiscount = totalPrice - totalDiscount
        val totalPayable = totalPrice - totalDiscount + deliveryCharge

        tv_total_price.text = "₹${"%.2f".format(totalPrice)}"
        tv_after_dis.text = "₹${"%.2f".format(afterDiscount)}"
        tv_discount_price.text = "₹${"%.2f".format(totalDiscount)}"
        tv_delivery_charges.text = "₹${"%.2f".format(deliveryCharge)}"
        tv_final_price.text = "₹${"%.2f".format(totalPayable)}"
        tv_total_amount.text = "₹${"%.2f".format(totalPayable)}"
        tv_savings.text = "You will save ₹${" %.2f".format(totalDiscount)} on this order"
        if(totalPayable != totalPrice+deliveryCharge){
            tv_amount.visibility = android.view.View.VISIBLE
            tv_amount.apply {
                text = "₹${String.format("%.2f", totalPrice+deliveryCharge)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCartItemClicked() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@AddToCartActivity)
            cartFood = db.messageDao().getAllMessages().toMutableList()
            setData()
            addToCartAdapter.updateCartItems(cartFood)
        }
    }

    override fun onDeleteCartItemClicked(id: Int) {
        TODO("Not yet implemented")
    }
}