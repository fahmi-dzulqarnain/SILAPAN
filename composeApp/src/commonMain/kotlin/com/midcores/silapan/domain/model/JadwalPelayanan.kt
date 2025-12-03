package com.midcores.silapan.domain.model

data class JadwalPelayanan(
    val id: String,
    val bulan: String,
    val tahun: String,
    val filePath: String,
    val createdAt: String,
    val updatedAt: String
)