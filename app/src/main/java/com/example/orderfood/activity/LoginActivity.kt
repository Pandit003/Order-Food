package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orderfood.RetrofitClient
import com.example.orderfood.AppConstants
import com.example.orderfood.R
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.net.ConnectivityManager

class LoginActivity : AppCompatActivity() {

    private var deviceId = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val wvAuth: WebView = findViewById(R.id.wvAuth)
        wvAuth.settings.javaScriptEnabled = true
        wvAuth.settings.javaScriptCanOpenWindowsAutomatically = true

        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val url = "https://auth.phone.email/log-in?client_id=${AppConstants.CLIENT_ID}" +
                "&auth_type=1&device=$deviceId"

        wvAuth.addJavascriptInterface(WebAppInterface(this), "Android")
        wvAuth.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("Login", "Loaded URL: $url")
            }
        }

        wvAuth.loadUrl(url)
    }

    fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetworkInfo
        return network != null && network.isConnected
    }

    fun getUserInfo(accessToken: String) {
        val call = RetrofitClient.RetrofitHelper.apiService.getUserInfo(
            accessToken,
            AppConstants.CLIENT_ID
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userInfo = response.body()!!
                    if (userInfo.isJsonObject && userInfo.get("status").asInt == 200) {
                        val phoneCountryCode = userInfo.get("country_code").asString
                        val phoneNumber = userInfo.get("phone_no").asString
                        val phJWT = userInfo.get("ph_email_jwt").asString

                        Log.d("Login", "Verified user: +$phoneCountryCode $phoneNumber")
                        Log.d("Login", "JWT: $phJWT")
                        Log.d("response", response.toString())
                        val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                        intent.putExtra("phone", phoneNumber)
                        startActivity(intent)
                    } else {
                        Log.w("Login", "Error retrieving user info: $userInfo")
                    }
                } else {
                    Log.w("Login", "API error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Login", "API failure: ${t.message}")
            }
        })
    }

    inner class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun sendTokenToApp(token: String) {
            runOnUiThread {
                Log.d("Login", "Received access token: $token")
                if (isConnected()) {
                    getUserInfo(token)
                } else {
                    Toast.makeText(context, "Please check your internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
