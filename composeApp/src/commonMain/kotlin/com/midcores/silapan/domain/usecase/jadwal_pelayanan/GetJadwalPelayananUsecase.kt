package com.midcores.silapan.domain.usecase.jadwal_pelayanan

import com.midcores.silapan.domain.model.JadwalPelayanan
import com.midcores.silapan.domain.repository.JadwalRepository

class GetJadwalPelayananUsecase(private val repo: JadwalRepository) {
    suspend operator fun invoke(year: String, refresh: Boolean = false): List<JadwalPelayanan> =
        repo.getLatest(year, refresh)
}