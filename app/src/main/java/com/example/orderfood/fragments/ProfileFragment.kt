package com.example.orderfood.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.orderfood.R
import com.example.orderfood.activity.LocationActivity

class ProfileFragment : Fragment() {
    lateinit var ll_order : LinearLayout
    lateinit var ll_address : LinearLayout
    lateinit var ll_edit_profile : LinearLayout
    lateinit var ll_contact_us : LinearLayout
    lateinit var tv_name : TextView
    lateinit var tv_phone : TextView
    lateinit var iv_personimage : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        ll_order = view.findViewById(R.id.ll_order)
        ll_address = view.findViewById(R.id.ll_address)
        ll_edit_profile = view.findViewById(R.id.ll_edit_profile)
        ll_contact_us = view.findViewById(R.id.ll_contact_us)
        tv_name = view.findViewById(R.id.tv_name)
        tv_phone = view.findViewById(R.id.tv_phone)
        iv_personimage = view.findViewById(R.id.iv_personimage)
        val sharedPreferences = requireContext().getSharedPreferences("Personal_Details", MODE_PRIVATE)
        var name = sharedPreferences.getString("name", "")
        var phone = sharedPreferences.getString("phone_number", "")
        var gender = sharedPreferences.getString("gender", "")
        tv_name.text = name
        tv_phone.text = phone
        if(gender.equals("boy")){
            iv_personimage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.boy))
        }else{
            iv_personimage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.girl))
        }
        ll_order.setOnClickListener {
            val newFragment = OrderFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container_body, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        ll_address.setOnClickListener{
            startActivity(Intent(requireContext(),LocationActivity::class.java))
        }
        ll_edit_profile.setOnClickListener {
            val newFragment = EditProfileFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container_body, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        ll_contact_us.setOnClickListener {
            val newFragment = ContactUsFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container_body, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Profile")
    }
}