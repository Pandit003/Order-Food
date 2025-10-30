package com.example.orderfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.AddToCartAdapter
import com.example.orderfood.adapter.OrderListAdapter
import com.example.orderfood.interfaces.OnOrderItemListener
import com.example.orderfood.model.OrderDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderFragment : Fragment() , OnOrderItemListener{
    private lateinit var rv_orderlist: RecyclerView
    private lateinit var orderListAdapter: OrderListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val json = requireContext().assets.open("order_list.json").bufferedReader().use { it.readText() }
        val orders: List<OrderDetails> = Gson().fromJson(json, object : TypeToken<List<OrderDetails>>() {}.type)

        rv_orderlist = view.findViewById(R.id.rv_orderlist)
        rv_orderlist.layoutManager = LinearLayoutManager(requireContext())
        orderListAdapter = OrderListAdapter(requireContext(), this,orders)
        rv_orderlist.adapter = orderListAdapter
        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("My Orders")
    }

    override fun onOrderItemClicked(id: String) {
        val newFragment = OrderDetailsFragment()
        var args = Bundle()
        args.putString("order_id", id)
        newFragment.arguments = args
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container_body, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}