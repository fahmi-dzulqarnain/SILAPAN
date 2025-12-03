package com.midcores.silapan.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GaleriImageDto(
    val id: String,
    @SerialName("url") val imageURL: String,
    @SerialName("added") val createdAt: String
)