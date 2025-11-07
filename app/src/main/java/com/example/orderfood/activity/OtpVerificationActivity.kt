package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaos.view.PinView
import com.example.orderfood.RetrofitClient
import com.example.orderfood.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.example.orderfood.AppConstants
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var otpView: PinView
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp_verification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        otpView = findViewById(R.id.pinview)
        auth = FirebaseAuth.getInstance()

        // Get verification ID from previous activity
        phoneNumber = intent.getStringExtra("phone") ?: ""

        findViewById<Button>(R.id.btn_verify).setOnClickListener {
            val otp = otpView.text.toString().trim()
            if (otp.length == 6) {
//                verifyCode(otp)
                verifyOtp(phoneNumber ,otp)
            } else {
                Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun verifyOtp(phoneNumber: String, otp: String) {
        val call = RetrofitClient.RetrofitHelper.apiService.verifyOtp(
            clientId = AppConstants.CLIENT_ID,
            phone = phoneNumber,
            otp = otp
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val json = response.body()
                    val accessToken = json?.get("access_token")?.asString
                    if (!accessToken.isNullOrEmpty()) {
                        getUserInfo(accessToken)
                    }
                } else {
                    Log.e("VerifyOTP", "Invalid OTP")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("VerifyOTP", "Error: ${t.message}")
            }
        })
    }

    /*private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }*/

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    // AUTO-FILL OTP
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code = credential.smsCode
            if (code != null) {
                otpView.setText(code)  // 🔥 Auto-fills PinView
//                verifyCode(code)
                verifyOtp(phoneNumber ,code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@OtpVerificationActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    fun getUserInfo(accessToken: String) {
        val call = RetrofitClient.RetrofitHelper.apiService.getUserInfo(
            accessToken,
            AppConstants.CLIENT_ID
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    val countryCode = userInfo?.get("country_code")?.asString
                    val phoneNumber = userInfo?.get("phone_no")?.asString
                    val jwt = userInfo?.get("ph_email_jwt")?.asString

                    Log.d("UserInfo", "Verified: $countryCode$phoneNumber | JWT: $jwt")

                    // Now validate JWT on your backend with API_KEY
                } else {
                    Log.e("UserInfo", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("UserInfo", "Error: ${t.message}")
            }
        })
    }
}