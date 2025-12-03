package com.midcores.silapan.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    val type: String,
    val name: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
    val photo: String?,
    @SerialName("updated_at") val updatedAt: String
)

@Serializable
data class AppProfileDto(
    val id: String,
    val title: String?,
    val description: String?,
    val location: String?,
    @SerialName("contact_info") val contactInfo: String?,
)