package com.example.orderfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class personalDetail(
    val name : String = "",
    val phone : String = "",
    val area : String = "",
    val landmark : String = "",
    val house_number : String = ""
): Parcelable