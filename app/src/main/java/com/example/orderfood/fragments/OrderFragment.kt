package com.example.orderfood.fragments

import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.OrderListAdapter
import com.example.orderfood.interfaces.OnOrderItemListener
import com.example.orderfood.model.OrderDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.widget.Toast
import android.util.Log

class OrderFragment : Fragment() , OnOrderItemListener{
    private lateinit var rv_orderlist: RecyclerView
    private lateinit var orderListAdapter: OrderListAdapter
    lateinit var db : FirebaseFirestore
    var name = ""
    var phone = ""
    lateinit var progress : ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
//        val json = requireContext().assets.open("order_list.json").bufferedReader().use { it.readText() }
//        val orders: List<OrderDetails> = Gson().fromJson(json, object : TypeToken<List<OrderDetails>>() {}.type)
        val sharedPreferences = requireContext().getSharedPreferences("Personal_Details", MODE_PRIVATE)
        name = sharedPreferences.getString("name", "").toString()
        phone = sharedPreferences.getString("phone_number", "").toString()
        db = FirebaseFirestore.getInstance()
        progress = ProgressDialog(view.context)
        progress.setCancelable(false)
        progress.setMessage("Loading...")
        progress.show()
        if(phone.equals("") || name.equals("")){
            val newFragment = EditProfileFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container_body, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            Toast.makeText(requireContext(),"Please fill up your details",Toast.LENGTH_SHORT).show()
            progress.dismiss()
        }else{
            fetchOrder()
        }
        rv_orderlist = view.findViewById(R.id.rv_orderlist)
        return view
    }

    private fun fetchOrder() {

        db.collection("OrderData")
            .document(phone)
            .collection("OrderDetails")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val orders = querySnapshot.toObjects(OrderDetails::class.java)
                if (orders.isNotEmpty()) {
                    rv_orderlist.layoutManager = LinearLayoutManager(requireContext())
                    orderListAdapter = OrderListAdapter(requireContext(), this,orders)
                    rv_orderlist.adapter = orderListAdapter
                } else {
                    Toast.makeText(requireContext(), "No orders found", Toast.LENGTH_SHORT).show()
                }
                progress.dismiss()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching orders", e)
                Toast.makeText(requireContext(), "Some thing went wrong try again", Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("My Orders")
    }

    override fun onOrderItemClicked(id: String, orderData : OrderDetails) {
        val newFragment = OrderDetailsFragment()
        var args = Bundle()
        args.putString("order_id", id)
        args.putParcelable("order_details", orderData)
        newFragment.arguments = args
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container_body, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}