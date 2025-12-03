package com.midcores.silapan.domain.usecase.galeri

import com.midcores.silapan.domain.model.Galeri
import com.midcores.silapan.domain.repository.GaleriRepository

class GetGaleriUsecase(private val repo: GaleriRepository) {
    suspend operator fun invoke(refresh: Boolean = false): List<Galeri> {
        return repo.getAll(refresh)
    }
}

class GetGaleriByCategoryUsecase(private val repo: GaleriRepository) {
    suspend operator fun invoke(category: String, refresh: Boolean = false) =
        repo.getByCategory(category)
}