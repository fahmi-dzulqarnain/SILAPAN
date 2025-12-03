package com.midcores.silapan.api.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val phone: String,
    val password: String
)