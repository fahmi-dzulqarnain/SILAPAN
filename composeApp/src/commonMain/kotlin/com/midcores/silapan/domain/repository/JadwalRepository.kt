package com.midcores.silapan.domain.repository

import com.midcores.silapan.domain.model.JadwalPelayanan

interface JadwalRepository {
    suspend fun getLatest(year: String, forceRefresh: Boolean = false): List<JadwalPelayanan>
}