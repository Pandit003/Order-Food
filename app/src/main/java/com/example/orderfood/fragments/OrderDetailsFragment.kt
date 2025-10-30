package com.example.orderfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.FoodCategoriesAdapter
import com.example.orderfood.adapter.OrderItemAdapter
import com.example.orderfood.interfaces.OnOrderItemListener
import com.example.orderfood.model.OrderDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderDetailsFragment : Fragment() , OnOrderItemListener{
    private lateinit var tv_order_status : TextView
    private lateinit var tv_resturant_name : TextView
    private lateinit var tv_resturant_address : TextView
    private lateinit var tv_order_id : TextView
    private lateinit var tv_total_price : TextView
    private lateinit var tv_discount_price : TextView
    private lateinit var tv_delivery_charges : TextView
    private lateinit var tv_final_price : TextView
    private lateinit var tv_your_name : TextView
    private lateinit var tv_your_number : TextView
    private lateinit var tv_payment_method : TextView
    private lateinit var tv_payment_date : TextView
    private lateinit var tv_delivery_address : TextView
    private lateinit var rv_item : RecyclerView
    var orderData : OrderDetails? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_details, container, false)
        tv_order_status = view.findViewById<TextView>(R.id.tv_order_status)
        tv_resturant_name = view.findViewById<TextView>(R.id.tv_resturant_name)
        tv_resturant_address = view.findViewById<TextView>(R.id.tv_resturant_address)
        tv_order_id = view.findViewById<TextView>(R.id.tv_order_id)
        tv_total_price = view.findViewById<TextView>(R.id.tv_total_price)
        tv_discount_price = view.findViewById<TextView>(R.id.tv_discount_price)
        tv_delivery_charges = view.findViewById<TextView>(R.id.tv_delivery_charges)
        tv_final_price = view.findViewById<TextView>(R.id.tv_final_price)
        tv_your_name = view.findViewById<TextView>(R.id.tv_your_name)
        tv_your_number = view.findViewById<TextView>(R.id.tv_your_number)
        tv_payment_method = view.findViewById<TextView>(R.id.tv_payment_method)
        tv_payment_date = view.findViewById<TextView>(R.id.tv_payment_date)
        tv_delivery_address = view.findViewById<TextView>(R.id.tv_delivery_address)
        rv_item = view.findViewById<RecyclerView>(R.id.rv_item)
        val json = requireContext().assets.open("order_list.json").bufferedReader().use { it.readText() }
        val orders: List<OrderDetails> = Gson().fromJson(json, object : TypeToken<List<OrderDetails>>() {}.type)
        var orderId = arguments?.getString("order_id") ?: 0
        for (order in orders){
            if (order.orderId == orderId){
                tv_order_status.text = order.orderStatus
                tv_resturant_name.text = order.restaurantName
                tv_resturant_address.text = order.restaurantAddress
                tv_order_id.text = "Order Id : ${order.orderId}"
                tv_total_price.text = "₹"+order.totalAmount.toString()
                tv_discount_price.text = "₹"+order.discountAmount.toString()
                tv_delivery_charges.text = "₹"+order.deliveryCharge.toString()
                tv_final_price.text = "₹"+order.finalAmount.toString()
                tv_your_name.text = order.deliveryPersonName
                tv_your_number.text = order.deliveryPersonPhone
                tv_payment_method.text = "Paid via: ${order.paymentMethod}"
                tv_payment_date.text = order.orderDate
                tv_delivery_address.text = order.deliveryAddress

                rv_item.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                rv_item.adapter = OrderItemAdapter(order.items)

            }
        }
        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Order Details")
    }

    override fun onOrderItemClicked(order_id: String) {

    }
}