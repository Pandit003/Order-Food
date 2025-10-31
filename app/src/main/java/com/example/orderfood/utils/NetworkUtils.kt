package com.example.orderfood.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

object NetworkUtils {

    // Check if internet is available (Wi-Fi or Mobile Data)
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // Show error message if no internet
    fun checkConnectionOrShowError(context: Context): Boolean {
        return if (isInternetAvailable(context)) {
            true
        } else {
//            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
