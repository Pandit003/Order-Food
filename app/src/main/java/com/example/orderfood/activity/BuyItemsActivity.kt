package com.example.orderfood.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Paint
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
    override fun onResume() {
        val sharedPreferences = getSharedPreferences("NameAndAddress", MODE_PRIVATE)
        var area = sharedPreferences.getString("area", "")
        var house = sharedPreferences.getString("house_number", "")
        var name = sharedPreferences.getString("user_name", "")
        var phone = sharedPreferences.getString("phone_number", "")
        var landmark = sharedPreferences.getString("landmark", "")
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
        setData(foodList!!)

        btn_change_address.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
        btn_buy.setOnClickListener {
            val upiUri = ("upi://pay?pa=pardoompandit@oksbi" +   // your UPI ID
                    "&pn=Hotel Anishka" +                // payee name
                    "&mc=0000" +                          // optional merchant code
                    "&tid=" +                    // optional transaction id
                    "&tr=" +                  // optional transaction ref
                    "&tn=Payment for Food" +              // note/message
                    "&am=1.00" +                        // amount
                    "&cu=INR").toUri()

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = upiUri

            val chooser = Intent.createChooser(intent, "Pay with")
            try {
                upiLauncher.launch(chooser)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No UPI app found!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    val upiLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data?.getStringExtra("response")
        handleUPIResponse(data)
    }

    private fun handleUPIResponse(response: String?) {
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
        /*if(status.equals("success")){
            ll_order_details.visibility = View.GONE
            ll_place_order.visibility = View.GONE
            ll_payment_status.visibility = View.VISIBLE
            successAnim.visibility = View.VISIBLE
            successCelebration.visibility = View.VISIBLE
            ll_bottom_status.visibility = View.VISIBLE
            tv_transaction_id.setText(approvalRefNo)
        }else{
            ll_order_details.visibility = View.GONE
            ll_place_order.visibility = View.GONE
            successCelebration.setAnimation("error_occured.json")
            successCelebration.playAnimation()
            ll_payment_status.visibility = View.VISIBLE
            successCelebration.visibility = View.VISIBLE
            successAnim.visibility = View.GONE
            ll_bottom_status.visibility = View.GONE
            tv_payment_status.setText("Payment Failed!")
            tv_payment_status.setTextColor(ContextCompat.getColor(this, R.color.red))
        }*/
        when {
            status == "success" -> {
                ll_order_details.visibility = View.GONE
                ll_place_order.visibility = View.GONE
                view.visibility = View.GONE
                ll_payment_status.visibility = View.VISIBLE
                successAnim.visibility = View.VISIBLE
                successCelebration.visibility = View.VISIBLE
                ll_bottom_status.visibility = View.VISIBLE
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
            }
            else -> {
                Toast.makeText(this, "Payment Cancelled!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setData(foodList : List<FoodItem>) {
        var totalPrice = 0.0
        var totalDiscount = 0.0

        rv_SelectedItem = findViewById<RecyclerView>(R.id.rv_SelectedItem)
        rv_SelectedItem.layoutManager = LinearLayoutManager(this)
        rv_SelectedItem.adapter = SelectedItemAdapter(this,foodList)

        for (item in foodList) {
            val itemTotal = item.price * item.quantity
            val itemDiscount = (item.price * item.discountPercent / 100) * item.quantity

            totalPrice += itemTotal
            totalDiscount += itemDiscount
        }

        val deliveryCharge = if (totalPrice - totalDiscount >= 500) 0.0 else 50.0
        val afterDiscount = totalPrice - totalDiscount
        val totalPayable = totalPrice - totalDiscount + deliveryCharge

        tv_total_price.text = "₹${"%.2f".format(totalPrice)}"
        tv_discount_price.text = "₹${"%.2f".format(totalDiscount)}"
        tv_delivery_charges.text = "₹${"%.2f".format(deliveryCharge)}"
        tv_final_price.text = "₹${"%.2f".format(totalPayable)}"
        tv_total_amount.text = "₹${"%.2f".format(totalPayable)}"
        if(totalDiscount > 0) {
            tv_savings.visibility = android.view.View.VISIBLE
            tv_savings.text = "You will save ₹${" %.2f".format(totalDiscount)} on this order"
        }
        if(totalPayable != totalPrice+deliveryCharge){
            tv_amount.visibility = android.view.View.VISIBLE
            tv_amount.apply {
                text = "₹${String.format("%.2f", totalPrice+deliveryCharge)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }
}