package com.midcores.silapan.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JadwalPelayananDto(
    val id: String,
    val bulan: String,
    val tahun: String,
    @SerialName("file_path") val filePath: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)