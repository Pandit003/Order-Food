package com.example.orderfood.widgets

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.orderfood.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import android.widget.Toast

class ShowQRCode(
    var amount : Double
) : BottomSheetDialogFragment() {
    private var countDownTimer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.layout_show_qrcode, container, false)
    }
    var onPaymentDone: (() -> Unit)? = null
    var onPaymentcancel: (() -> Unit)? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.ivQrCode)

        val note = "Order for food"
        val upiUrl = "upi://pay?pa=Q311994831@ybl&pn=PhonePeMerchant&am=$amount&tn=$note&cu=INR"
        val qrBitmap = generateQRCode(upiUrl)
        imageView.setImageBitmap(qrBitmap)
        startCountdown(view)

        /*db.runTransaction {
            db.collection("OrderData").document(user)
                .collection("OrderDetails").add(orderDetails!!)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(
                        this,
                        msg,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Unable to insert the data",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }*/
    }
    private fun startCountdown(view: View) {
        val timerText = view.findViewById<TextView>(R.id.tvTimer)
        val cancelBtn = view.findViewById<Button>(R.id.btnCancel)
        val btnPaid = view.findViewById<Button>(R.id.btnPaid)

        // Cancel any existing timer before starting new one
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(2 * 60 * 1000, 1000) { // 2 minutes
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerText.text = "00:00"
                timerText.setTextColor(Color.GRAY)
                Toast.makeText(requireContext(), "Payment session expired", Toast.LENGTH_SHORT).show()
                dismiss()
                // Disable QR or return to previous screen if you want
            }
        }.start()
        cancelBtn.setOnClickListener {
            onPaymentcancel?.invoke()
            countDownTimer?.cancel()
            timerText.text = "Cancelled"
            timerText.setTextColor(Color.GRAY)
            Toast.makeText(requireContext(), "Payment cancelled", Toast.LENGTH_SHORT).show()
            dismiss()
            // Optionally finish activity:
            // finish()
        }
        btnPaid.setOnClickListener {
            onPaymentDone?.invoke()
            countDownTimer?.cancel()
            timerText.text = "Processing.."
            timerText.setTextColor(Color.GRAY)
//            Toast.makeText(requireContext(), "Payment Processing...", Toast.LENGTH_SHORT).show()
            dismiss()
            // Optionally finish activity:
            // finish()
        }
    }
    private fun generateQRCode(text: String): Bitmap? {
        val size = 800
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, 1)
        }

        return try {
            val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(x, y, if (bits[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        bottomSheet.requestLayout()
        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
        behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = false
        behavior.isDraggable = false

        behavior.peekHeight = 0
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

    }


    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}
