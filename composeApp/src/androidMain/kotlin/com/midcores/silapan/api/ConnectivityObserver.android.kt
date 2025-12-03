package com.midcores.silapan.api

import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class ConnectivityObserver : KoinComponent {

    private val context: Context by inject()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    actual val isNetworkAvailable: StateFlow<Boolean> = callbackFlow {
        // 2. Create the network callback
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true) // Network is available
            }

            override fun onLost(network: Network) {
                trySend(false) // Network is lost
            }

            override fun onUnavailable() {
                trySend(false) // Network is unavailable
            }
        }

        // 3. Register the callback
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // 4. Set initial state
        val isInitiallyOnline = connectivityManager.activeNetwork != null
        trySend(isInitiallyOnline)

        // 5. Unregister on flow cancellation
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.stateIn(
        scope = getKoin().get(), // Use Koin's CoroutineScope
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = true // Assume online initially
    )
}