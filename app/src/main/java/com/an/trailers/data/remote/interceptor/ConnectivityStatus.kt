package com.an.trailers.data.remote.interceptor

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.RemoteException

class ConnectivityStatus(base: Context) : ContextWrapper(base) {
    companion object {
        fun isConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return try {
                manager.getNetworkCapabilities(manager.activeNetwork)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .let { connected ->
                        connected == true
                    }
            } catch (e: RemoteException) {
                e.printStackTrace()
                false
            }
        }
    }
}
