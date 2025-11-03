package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orderfood.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var et_phone : EditText
    private lateinit var btn_continue : Button
    private lateinit var verificationId: String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        et_phone = findViewById(R.id.et_phone)
        btn_continue = findViewById(R.id.btn_continue)
        auth = FirebaseAuth.getInstance()
        btn_continue.setOnClickListener {
            val phone = et_phone.text.toString().trim()

            if (phone.length != 10) {
                et_phone.error = "Enter valid 10-digit phone number"
                return@setOnClickListener
            }

            val fullPhone = "+91$phone"
            sendVerificationCode(fullPhone)
        }
    }
    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification or instant verification
                    val smsCode = credential.smsCode
                    if (smsCode != null) {
                        goToOtpScreen(phoneNumber, smsCode)
                    } else {
                        goToOtpScreen(phoneNumber, "")
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@LoginActivity, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@LoginActivity.verificationId = verificationId
                    goToOtpScreen(phoneNumber, "")
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun goToOtpScreen(phoneNumber: String, code: String) {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra("phone", phoneNumber)
        intent.putExtra("verificationId", verificationId)
        intent.putExtra("code", code)
        startActivity(intent)
    }
}