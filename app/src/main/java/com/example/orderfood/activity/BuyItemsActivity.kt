package com.example.orderfood.activity

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.SelectedItemAdapter
import com.example.orderfood.model.FoodItem
import androidx.core.net.toUri
import com.airbnb.lottie.LottieAnimationView
import com.example.orderfood.model.OrderDetails
import com.example.orderfood.model.OrderItem
import com.example.orderfood.widgets.FoodDetailBottomSheet
import com.example.orderfood.widgets.ShowQRCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class BuyItemsActivity() : AppCompatActivity() {
    private lateinit var rv_SelectedItem : RecyclerView
    private lateinit var btn_change_address : TextView
    private lateinit var tv_name : TextView
    private lateinit var tv_address : TextView
    private lateinit var tv_district : TextView
    private lateinit var tv_phone : TextView
    private lateinit var tv_total_price: TextView
    private lateinit var tv_discount_price: TextView
    private lateinit var tv_delivery_charges: TextView
    private lateinit var tv_final_price: TextView
    private lateinit var tv_total_amount: TextView
    private lateinit var tv_savings: TextView
    private lateinit var tv_amount: TextView
    private lateinit var tv_payment_status: TextView
    private lateinit var tv_paid_amount: TextView
    private lateinit var tv_transaction_id: TextView
    private lateinit var view: View
    private lateinit var btn_buy: Button
    private lateinit var ll_order_details: LinearLayout
    private lateinit var ll_place_order: LinearLayout
    private lateinit var ll_payment_status: LinearLayout
    private lateinit var ll_bottom_status: LinearLayout
    private lateinit var successAnim: LottieAnimationView
    private lateinit var successCelebration: LottieAnimationView
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    var orderDetails : OrderDetails? = null
    var name = "" ;
    var phone = "" ;
    var area = "" ;
    var house = "" ;
    var landmark = "" ;
    var user = ""
    var QrtotalAmount = 0.0
    override fun onResume() {
        val sharedPreferences = getSharedPreferences("NameAndAddress", MODE_PRIVATE)
        area = sharedPreferences.getString("area", "").toString()
        house = sharedPreferences.getString("house_number", "").toString()
        name = sharedPreferences.getString("user_name", "").toString()
        phone = sharedPreferences.getString("phone_number", "").toString()
        landmark = sharedPreferences.getString("landmark", "").toString()
        tv_name.setText(name)
        tv_address.setText("$house, $area, $landmark")
        tv_district.setText("Raigarh, Chhattisgarh")
        tv_phone.setText(phone)
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buy_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val foodList = intent.getParcelableArrayListExtra<FoodItem>("foodList")
        btn_change_address = findViewById<TextView>(R.id.btn_change_address)
        tv_name = findViewById<TextView>(R.id.tv_name)
        tv_address = findViewById<TextView>(R.id.tv_address)
        tv_district = findViewById<TextView>(R.id.tv_district)
        tv_phone = findViewById<TextView>(R.id.tv_phone)
        tv_total_price = findViewById(R.id.tv_total_price)
        tv_discount_price = findViewById(R.id.tv_discount_price)
        tv_delivery_charges = findViewById(R.id.tv_delivery_charges)
        tv_final_price = findViewById(R.id.tv_final_price)
        tv_total_amount = findViewById(R.id.tv_total_amount)
        btn_buy = findViewById(R.id.btn_buy)
        tv_amount = findViewById(R.id.tv_amount)
        tv_payment_status = findViewById(R.id.tv_payment_status)
        tv_paid_amount = findViewById(R.id.tv_paid_amount)
        tv_transaction_id = findViewById(R.id.tv_transaction_id)
        view = findViewById(R.id.view)
        tv_savings = findViewById(R.id.tv_savings)
        ll_order_details = findViewById(R.id.ll_order_details)
        ll_place_order = findViewById(R.id.ll_place_order)
        ll_payment_status = findViewById(R.id.ll_payment_status)
        ll_bottom_status = findViewById(R.id.ll_bottom_status)
        successAnim = findViewById(R.id.successAnim)
        successCelebration = findViewById(R.id.successCelebration)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser?.uid.toString()
        btn_change_address.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }

        btn_buy.setOnClickListener {
            if(tv_name.text.isEmpty() || tv_address.text.isEmpty() || tv_phone.text.isEmpty()){
                Toast.makeText(this,"Please enter name, phone and address details",Toast.LENGTH_SHORT).show()
            }else {
                val sheet = ShowQRCode(QrtotalAmount)
                sheet.onPaymentDone = {
                    addDataToFireStore()
                }
                sheet.show(supportFragmentManager, "Show QR Code")
            }

        }
        val progress = ProgressDialog(this)
        progress.setCancelable(false)
        progress.setMessage("Loading...")
        progress.show()

        val counterRef = db.collection("metadata").document("order_counter")
        db.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val lastOrder = snapshot.getLong("last_order") ?: 1023
            val newOrder = lastOrder + 1
            transaction.update(counterRef, "last_order", newOrder)
            newOrder
        }.addOnSuccessListener { newOrder ->
            val orderId = "ORD$newOrder"
            setData(foodList!!,orderId)
            progress.dismiss()
        }.addOnFailureListener {
            progress.dismiss()
            onBackPressed()
            Toast.makeText(this,"Some thing went wrong try again",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDataToFireStore() {
        /*var msg = ""
        var orderstatus = ""
        if(status.equals("SUCCESS")) {
            msg = "Order placed successfully!"
            orderstatus = "Preparing"
        } else {
            msg = "Order cancelled. Payment failed."
            orderstatus = "Cancelled"
        }*/
        orderDetails = orderDetails?.copy(
            paymentMethod = "UPI",
            paymentStatus = "Processing",
            orderStatus = "Processing",
            deliveryTime = "30 mins",
            estimatedDeliveryMinutes = 30
        )

        db.runTransaction {
            db.collection("OrderData").document(tv_phone.text.toString())
                .collection("OrderDetails").add(orderDetails!!)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(
                        this,
                        "We’re processing your order...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Unable to insert the data",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }


    private fun setData(foodList : List<FoodItem>,orderid:String) {
        var totalPrice = 0.0
        var totalDiscount = 0.0
        val orderItemsList = mutableListOf<OrderItem>()
        rv_SelectedItem = findViewById<RecyclerView>(R.id.rv_SelectedItem)
        rv_SelectedItem.layoutManager = LinearLayoutManager(this)
        rv_SelectedItem.adapter = SelectedItemAdapter(this,foodList)

        for (item in foodList) {
            val itemTotal = item.price * item.quantity
            val itemDiscount = (item.price * item.discountPercent / 100) * item.quantity

            totalPrice += itemTotal
            totalDiscount += itemDiscount

            val orderItem = OrderItem(
                itemId = item.id.toString(),
                name = item.name,
                quantity = item.quantity,
                pricePerItem = item.price,
                imageUrl = item.imageUrl,
                totalPrice = item.finalPrice
            )
            orderItemsList.add(orderItem)
        }

        val deliveryCharge = if (totalPrice - totalDiscount >= 500) 0.0 else 50.0
        val afterDiscount = totalPrice - totalDiscount
        val totalPayable = totalPrice - totalDiscount + deliveryCharge

        tv_total_price.text = "₹${"%.2f".format(totalPrice)}"
        tv_discount_price.text = "₹${"%.2f".format(totalDiscount)}"
        tv_delivery_charges.text = "₹${"%.2f".format(deliveryCharge)}"
        tv_final_price.text = "₹${"%.2f".format(totalPayable)}"
        tv_total_amount.text = "₹${"%.2f".format(totalPayable)}"
        QrtotalAmount = totalPayable
        if(totalDiscount > 0) {
            tv_savings.visibility = View.VISIBLE
            tv_savings.text = "You will save ₹${" %.2f".format(totalDiscount)} on this order"
        }
        if(totalPayable != totalPrice+deliveryCharge){
            tv_amount.visibility = View.VISIBLE
            tv_amount.apply {
                text = "₹${String.format("%.2f", totalPrice+deliveryCharge)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }


        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        orderDetails = OrderDetails(
            orderId = orderid,
            userId = name,
            restaurantName = "Hotel Anishka",
            restaurantAddress = "Chhatisgargh, Raigarh",
            items = orderItemsList,
            totalAmount = totalPrice,
            discountAmount = totalDiscount,
            deliveryCharge = deliveryCharge,
            finalAmount = totalPayable,
            orderDate = LocalDateTime.now().format(formatter),
            deliveryTime = "30 mins",
            estimatedDeliveryMinutes = 30,
            paymentMethod = "",
            paymentStatus = "PENDING",
            orderStatus = "",
            deliveryAddress = tv_address.text.toString(),
            deliveryPersonName = "Prasanjeet chatterjee",
            deliveryPersonPhone = "9148754100",
            specialInstructions = "",
            rating = 0f,
            feedback = "",
        )
    }
    /*private fun handleUPIResponse(response: String?) {
        if (response == null) {
            Toast.makeText(this, "Payment cancelled!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = response.split("&")
        var status = ""
        var approvalRefNo = ""

        for (param in data) {
            val parts = param.split("=")
            if (parts.size >= 2) {
                when (parts[0].lowercase()) {
                    "status" -> status = parts[1].lowercase()
                    "approvalrefno", "txnref" -> approvalRefNo = parts[1]
                }
            }
        }
        when {
            status == "success" -> {
                ll_order_details.visibility = View.GONE
                ll_place_order.visibility = View.GONE
                view.visibility = View.GONE
                ll_payment_status.visibility = View.VISIBLE
                successAnim.visibility = View.VISIBLE
                successCelebration.visibility = View.VISIBLE
                ll_bottom_status.visibility = View.VISIBLE
                addDataToFireStore("SUCCESS")
            }
            status.contains("fail") -> {
                ll_order_details.visibility = View.GONE
                ll_place_order.visibility = View.GONE
                view.visibility = View.GONE
                successCelebration.setAnimation("error_occured.json")
                successCelebration.playAnimation()
                successCelebration.visibility = View.VISIBLE
                ll_payment_status.visibility = View.VISIBLE
                successAnim.visibility = View.GONE
                ll_bottom_status.visibility = View.GONE
                tv_payment_status.setText("Payment Failed!")
                tv_payment_status.setTextColor(ContextCompat.getColor(this, R.color.red))
                val params = successCelebration.layoutParams
                params.width = 300   // in pixels
                params.height = 300  // in pixels
                successCelebration.layoutParams = params
                Handler(Looper.getMainLooper()).postDelayed({
                    onBackPressedDispatcher.onBackPressed()
                }, 3000)
                addDataToFireStore("FAILED")
            }
            else -> {
                Toast.makeText(this, "Payment Cancelled!", Toast.LENGTH_SHORT).show()
                addDataToFireStore("CANCEL")
            }
        }
    }*/
}