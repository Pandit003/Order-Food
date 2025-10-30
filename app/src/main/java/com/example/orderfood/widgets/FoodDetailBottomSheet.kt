package com.example.orderfood.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.localDatabase.AppDatabase
import com.example.orderfood.model.CartItems
import com.example.orderfood.model.FoodItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class FoodDetailBottomSheet(
    var foodItems : List<FoodItem>,val pos : Int
) : BottomSheetDialogFragment() {
    private lateinit var tv_food_name: TextView
    private lateinit var tv_food_desc: TextView
    private lateinit var tv_price: TextView
    private lateinit var iv_food: ImageView
    private lateinit var iv_close: ImageView
    private lateinit var btn_add_to_cart: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_food_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_food_name = view.findViewById<TextView>(R.id.tv_food_name)
        tv_food_desc = view.findViewById<TextView>(R.id.tv_food_desc)
        tv_price = view.findViewById<TextView>(R.id.tv_price)
        iv_food = view.findViewById<ImageView>(R.id.iv_food)
        iv_close = view.findViewById<ImageView>(R.id.iv_close)
        btn_add_to_cart = view.findViewById<Button>(R.id.btn_add_to_cart)
        tv_food_name.text = foodItems.get(pos).name
        tv_food_desc.text = foodItems.get(pos).description
        Glide.with(requireContext()).load(foodItems.get(pos).imageUrl).into(iv_food)
        tv_price.text = "Only ₹ "+foodItems.get(pos).price.toString()
        iv_close.setOnClickListener { dismiss() }

        btn_add_to_cart.setOnClickListener {
            val db = AppDatabase.getDatabase(this.requireContext())
            lifecycleScope.launch {
//                db.messageDao()searchById(foodItems[pos].id)
                db.messageDao().insertMessage(foodItems[pos].toCartItem())
            }
            Toast.makeText(this.requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    private fun FoodItem.toCartItem(): CartItems {
        return CartItems(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            categoryId = this.categoryId,
            price = this.price,
            discountPercent = this.discountPercent,
            finalPrice = this.finalPrice,
            isAvailable = this.isAvailable,
            rating = this.rating,
            totalRatings = this.totalRatings,
            quantity = 1
        )
    }



    override fun onStart() {
        super.onStart()

        // Access the dialog safely
        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        bottomSheet.requestLayout()
        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
        behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = true

        behavior.peekHeight = 0
    }


    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}
