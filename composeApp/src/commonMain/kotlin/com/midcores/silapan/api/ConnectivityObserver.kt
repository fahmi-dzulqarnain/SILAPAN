package com.midcores.silapan.api

import kotlinx.coroutines.flow.StateFlow

expect class ConnectivityObserver() {
    val isNetworkAvailable: StateFlow<Boolean>
}