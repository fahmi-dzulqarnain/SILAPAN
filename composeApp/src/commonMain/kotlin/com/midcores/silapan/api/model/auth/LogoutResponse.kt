package com.midcores.silapan.api.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val message: String
)