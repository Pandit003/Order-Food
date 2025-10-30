package com.example.orderfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.orderfood.R

class ProfileFragment : Fragment() {
    lateinit var ll_order : LinearLayout
    lateinit var ll_address : LinearLayout
    lateinit var ll_edit_profile : LinearLayout
    lateinit var ll_contact_us : LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        ll_order = view.findViewById(R.id.ll_order)
        ll_address = view.findViewById(R.id.ll_address)
        ll_edit_profile = view.findViewById(R.id.ll_edit_profile)
        ll_contact_us = view.findViewById(R.id.ll_contact_us)

        ll_order.setOnClickListener {
            val newFragment = OrderFragment()
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