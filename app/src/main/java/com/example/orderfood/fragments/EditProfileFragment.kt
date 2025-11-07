package com.example.orderfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.orderfood.R

class EditProfileFragment : Fragment() {
    private lateinit var btn_save : Button
    private lateinit var et_name : EditText
    private lateinit var et_phone_no : EditText
    private lateinit var iv_edit_boy : ImageView
    private lateinit var iv_edit_girl : ImageView
    private lateinit var rl_boy : RelativeLayout
    private lateinit var rl_girl : RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        btn_save = view.findViewById(R.id.btn_save)
        et_name = view.findViewById(R.id.et_name)
        et_phone_no = view.findViewById(R.id.et_phone_no)
        iv_edit_boy = view.findViewById(R.id.iv_edit_boy)
        iv_edit_girl = view.findViewById(R.id.iv_edit_girl)
        rl_boy = view.findViewById(R.id.rl_boy)
        rl_girl = view.findViewById(R.id.rl_girl)
        val sharedPreferences = requireContext().getSharedPreferences("Personal_Details", MODE_PRIVATE)
        var name = sharedPreferences.getString("name", "")
        var phone = sharedPreferences.getString("phone_number", "")
        var gender = sharedPreferences.getString("gender", "boy")
        if(gender.equals("boy")){
            rl_boy.visibility = View.VISIBLE
            rl_girl.visibility = View.GONE
        }else{
            rl_boy.visibility = View.GONE
            rl_girl.visibility = View.VISIBLE
        }
        et_name.setText(name)
        et_phone_no.setText(phone)
        btn_save.setOnClickListener {
            when {
                et_name.text.isEmpty() -> {
                    et_name.error = "Please enter name"
                    et_name.requestFocus()
                }

                et_phone_no.text.isEmpty() -> {
                    et_phone_no.error = "Please enter phone number"
                    et_phone_no.requestFocus()
                }
                et_phone_no.text.length != 10 ->{
                    et_phone_no.error = "Please enter valid phone number"
                    et_phone_no.requestFocus()
                }
                else -> {
                    val sharedPreferences =
                        requireContext().getSharedPreferences("Personal_Details", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("name", et_name.text.toString())
                    editor.putString("phone_number", et_phone_no.text.toString())
                    editor.putString("gender", if (rl_boy.isVisible) "boy" else "girl")
                    editor.apply()
                    Toast.makeText(requireContext(),"Profile Saved",Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
        iv_edit_boy.setOnClickListener {
            rl_girl.visibility = View.VISIBLE
            iv_edit_boy.visibility = View.GONE
            iv_edit_girl.visibility = View.GONE
        }
        iv_edit_girl.setOnClickListener {
            rl_boy.visibility = View.VISIBLE
            iv_edit_boy.visibility = View.GONE
            iv_edit_girl.visibility = View.GONE
        }

        rl_girl.setOnClickListener {
            rl_boy.visibility = View.GONE
            iv_edit_girl.visibility = View.VISIBLE
        }
        rl_boy.setOnClickListener {
            rl_girl.visibility = View.GONE
            iv_edit_boy.visibility = View.VISIBLE
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Edit Profile")
    }
}