package com.midcores.silapan.domain.model

data class Galeri(
    val id: String,
    val title: String,
    val description: String?,
    val featureImage: String?,
    val category: String?,
    val images: List<GaleriImage>,
    val createdAt: String
)