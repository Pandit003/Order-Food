package com.example.orderfood.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orderfood.R
import com.example.orderfood.utils.NetworkUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class LocationActivity : AppCompatActivity() {
    private lateinit var et_name : EditText
    private lateinit var et_phone_no : EditText
    private lateinit var et_house_no : EditText
    private lateinit var et_area : EditText
    private lateinit var et_landmark : EditText
    private lateinit var btn_save : Button
    private lateinit var fl_getLocation : FrameLayout
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkLocationPermission()
        val sharedPreferences = getSharedPreferences("NameAndAddress", MODE_PRIVATE)
        var area = sharedPreferences.getString("area", "")
        var house = sharedPreferences.getString("house_number", "")
        var name = sharedPreferences.getString("user_name", "")
        var phone = sharedPreferences.getString("phone_number", "")
        var landmark = sharedPreferences.getString("landmark", "")
//        if(name?.isEmpty() || phone?.isEmpty()) {
            val personalSharedPreferences = getSharedPreferences("Personal_Details", MODE_PRIVATE)
            var Profilename = personalSharedPreferences.getString("name", "")
            var Profilephone = personalSharedPreferences.getString("phone_number", "")
//        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        et_name = findViewById(R.id.et_name)
        et_phone_no = findViewById(R.id.et_phone_no)
        et_house_no = findViewById(R.id.et_house_no)
        et_area = findViewById(R.id.et_area)
        et_landmark = findViewById(R.id.et_landmark)
        btn_save = findViewById(R.id.btn_save)
        fl_getLocation = findViewById(R.id.fl_getLocation)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        et_name.setText(if(name?.isEmpty() == true) Profilename else name)
        et_phone_no.setText(if(phone?.isEmpty() == true) Profilephone else phone)
        et_area.setText(area)
        et_landmark.setText(landmark)
        et_house_no.setText(house)
        btn_save.setOnClickListener {
            // Handle save button click
            btn_save.isEnabled = false
            val name = et_name.text.toString()
            val phoneNo = et_phone_no.text.toString()
            val houseNo = et_house_no.text.toString()
            val area = et_area.text.toString()
            val landmark = et_landmark.text.toString()
            when {
                name.isEmpty() -> {
                    et_name.error = "Please enter name"
                    et_name.requestFocus()
                }
                phoneNo.isEmpty() -> {
                    et_phone_no.error = "Please enter phone number"
                    et_phone_no.requestFocus()
                }
                phoneNo.length != 10-> {
                    et_phone_no.error = "Please enter valid phone number"
                    et_phone_no.requestFocus()
                }
                houseNo.isEmpty() -> {
                    et_house_no.error = "Please enter house number"
                    et_house_no.requestFocus()
                }
                area.isEmpty() -> {
                    et_area.error = "Please enter area"
                    et_area.requestFocus()
                }
                else -> {
                    val sharedPreferences = getSharedPreferences("NameAndAddress", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user_name", et_name.text.toString())
                    editor.putString("phone_number", et_phone_no.text.toString())
                    editor.putString("area", et_area.text.toString())
                    editor.putString("landmark", et_landmark.text.toString())
                    editor.putString("house_number", et_house_no.text.toString())
                    editor.apply()
                    Toast.makeText(this,"Address Saved",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }
        }
        fl_getLocation.setOnClickListener{
            if (NetworkUtils.checkConnectionOrShowError(this)) {
                fl_getLocation.isEnabled = false
                getLocation()
            } else {
                Toast.makeText(this@LocationActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Edit Address"
    }
    fun getLocation(){
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Update interval in milliseconds
            fastestInterval = 5000 // Fastest interval for updates
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val geocoder = Geocoder(this@LocationActivity, Locale.getDefault())
                    try {
                        val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                        val fullAddress = address?.getAddressLine(0)
                        val parts = fullAddress?.split(",")?.map { it.trim() } ?: emptyList()
                        val houseNo = parts.getOrNull(0) ?: ""
                        val area = listOfNotNull(parts.getOrNull(1), parts.getOrNull(2)).joinToString(", ")
                        if (address?.adminArea.equals("Chhattisgarh", ignoreCase = true)
                            && address?.locality.equals("Raigarh", ignoreCase = true)) {

                            et_house_no.setText(houseNo)
                            et_area.setText(area)
                        } else {
                            Toast.makeText(
                                this@LocationActivity,
                                "Service not available outside Raigarh, Chhattisgarh",
                                Toast.LENGTH_LONG
                            ).show()
                            fl_getLocation.isEnabled = true

                        }
                    }catch (e : IOException){
                        Toast.makeText(this@LocationActivity, "Unable to fetch location details", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onLocationResult: "+e)
                        fl_getLocation.isEnabled = true
                    }
                    // Process new latitude and longitude
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
//            getLocation()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}