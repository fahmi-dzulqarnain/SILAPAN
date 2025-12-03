package com.midcores.silapan.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GaleriDto(
    val id: String,
    val title: String,
    val description: String?,
    @SerialName("feature_image") val featureImage: String?,
    val category: String?,
    val images: List<GaleriImageDto> = emptyList(),
    @SerialName("created_at") val createdAt: String
)