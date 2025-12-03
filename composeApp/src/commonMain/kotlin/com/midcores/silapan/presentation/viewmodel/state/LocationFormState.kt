package com.midcores.silapan.presentation.viewmodel.state

import dev.jordond.compass.permissions.PermissionState

data class LocationFormState(
    val addressLabel: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val permissionStatus: PermissionState = PermissionState.NotDetermined
)