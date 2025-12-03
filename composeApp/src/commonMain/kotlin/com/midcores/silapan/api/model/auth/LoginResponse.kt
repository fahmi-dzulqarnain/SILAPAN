package com.midcores.silapan.api.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserDTO
)