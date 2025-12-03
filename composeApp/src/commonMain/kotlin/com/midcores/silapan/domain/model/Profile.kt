package com.midcores.silapan.domain.model

data class Profile(
    val id: String,
    val type: String,
    val name: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
    val photo: String?,
    val updatedAt: String?
)

data class AppProfile(
    val title: String,
    val description: String?,
    val contactInfo: String?,
    val location: String?
)