package com.midcores.silapan.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.midcores.silapan.presentation.viewmodel.state.LocationFormState
import dev.jordond.compass.Place
import dev.jordond.compass.Priority
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.MobileGeolocator
import dev.jordond.compass.permissions.MobileLocationPermissionController
import dev.jordond.compass.permissions.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationFormViewModel : ScreenModel {

    private val permissions = MobileLocationPermissionController()
    private val geolocation = MobileGeolocator(permissions)

    var uiState by mutableStateOf(LocationFormState())
        private set

    fun updateAddressLabel(
        addressLabel: String,
        latitude: String,
        longitude: String
    ) {
        uiState = uiState.copy(
            addressLabel = addressLabel,
            latitude = latitude,
            longitude = longitude
        )
    }

    suspend fun requestLocation() {
        coroutineScope {
            uiState = uiState.copy(isLoading = true, error = null)

            val status = permissions.requirePermissionFor(Priority.HighAccuracy)
            uiState = uiState.copy(permissionStatus = status)

            if (status == PermissionState.NotDetermined) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Location permission is required."
                )
                return@coroutineScope
            }

            val locationResult = geolocation.current()

            locationResult
                .onSuccess { location ->
                    val latitude = location.coordinates.latitude
                    val longitude = location.coordinates.longitude

                    launch {
                        val addressLabel = getPlaceFromCoordinates(latitude, longitude)

                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                addressLabel = addressLabel?.name ?: "$latitude, $longitude",
                                latitude = "$latitude",
                                longitude = "$longitude"
                            )
                        }
                    }
                }
                .onFailed { exception ->
                    uiState = uiState.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }

    private suspend fun getPlaceFromCoordinates(lat: Double, lng: Double): Place? =
        Geocoder().placeOrNull(lat, lng)
}