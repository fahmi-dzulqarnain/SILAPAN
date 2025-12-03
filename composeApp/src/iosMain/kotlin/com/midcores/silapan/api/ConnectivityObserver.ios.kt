package com.midcores.silapan.api

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_monitor_start

actual class ConnectivityObserver : KoinComponent {

    actual val isNetworkAvailable: StateFlow<Boolean> = callbackFlow {
        val monitor = nw_path_monitor_create()

        nw_path_monitor_set_update_handler(monitor) { path ->
            val isOnline = nw_path_get_status(path) == nw_path_status_satisfied
            trySend(isOnline)
        }

        nw_path_monitor_start(monitor)

        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }.stateIn(
        scope = getKoin().get(),
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
}