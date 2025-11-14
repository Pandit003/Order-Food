package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.OrderListAdapter
import com.example.orderfood.adapter.PendingOrderListAdapter
import com.example.orderfood.interfaces.OnOrderItemListener
import com.example.orderfood.interfaces.OnOrderRequestListener
import com.example.orderfood.model.OrderDetails
import com.example.orderfood.model.personalDetail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class AdminActivity : AppCompatActivity(), OnOrderRequestListener {
    private lateinit var etSearch: EditText
    private lateinit var ll_orderData: LinearLayout
    private lateinit var ll_pending_order: LinearLayout
    private lateinit var ll_processing_order: LinearLayout
    private lateinit var ll_complete_order: LinearLayout
    private lateinit var rv_order_data: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var pendingOrderListAdapter: PendingOrderListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("FCM", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FCM", "Token: $token")
            }
        db = FirebaseFirestore.getInstance()
        etSearch = findViewById(R.id.et_search)
        ll_orderData = findViewById(R.id.ll_orderData)
        ll_pending_order = findViewById(R.id.ll_pending_order)
        ll_processing_order = findViewById(R.id.ll_processing_order)
        ll_complete_order = findViewById(R.id.ll_complete_order)
        rv_order_data = findViewById(R.id.rv_order_data)

        val iv_search : ImageView = findViewById(R.id.iv_search)
        val iv_back : ImageView = findViewById(R.id.iv_back)
        iv_search.isVisible = true
        iv_back.isVisible = false
        etSearch.isFocusable = false
        etSearch.setOnClickListener{
            val intent = Intent(this, SearchPhoneNoActivity::class.java)
            startActivity(intent)
        }
        ll_pending_order.setOnClickListener {
            db.collectionGroup("OrderDetails")
                .get()
                .addOnSuccessListener { snapshot ->
                    val allOrders = snapshot.toObjects(OrderDetails::class.java)
                    rv_order_data.layoutManager = LinearLayoutManager(this)
                    pendingOrderListAdapter = PendingOrderListAdapter(this, this,allOrders)
                    rv_order_data.adapter = pendingOrderListAdapter

                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching all orders", e)
                }

        }
    }

    override fun onOrderRequestClicked() {
        TODO("Not yet implemented")
    }
}