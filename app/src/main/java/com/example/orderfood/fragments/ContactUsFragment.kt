package com.example.orderfood.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.example.orderfood.R

class ContactUsFragment : Fragment() {
    private lateinit var tv_whatsapp_number : TextView
    private lateinit var tv_phone_number : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_us, container, false)
        tv_whatsapp_number = view.findViewById(R.id.tv_whatsapp_number)
        tv_phone_number = view.findViewById(R.id.tv_phone_number)
        tv_whatsapp_number.setOnClickListener {
            val url = "https://wa.me/918144542955"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        tv_phone_number.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918144542955"))
            startActivity(intent)
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Contact Us")
    }
}