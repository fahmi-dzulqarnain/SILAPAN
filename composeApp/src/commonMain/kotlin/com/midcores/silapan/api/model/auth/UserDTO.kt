package com.midcores.silapan.api.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String
)