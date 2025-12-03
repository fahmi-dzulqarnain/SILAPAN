package com.midcores.silapan.domain.repository

import com.midcores.silapan.domain.model.Galeri

interface GaleriRepository {
    suspend fun getAll(forceRefresh: Boolean = false): List<Galeri>
    suspend fun getByCategory(category: String): List<Galeri>
}