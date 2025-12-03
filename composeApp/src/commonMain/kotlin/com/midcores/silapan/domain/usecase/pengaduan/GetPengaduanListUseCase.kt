package com.midcores.silapan.domain.usecase.pengaduan

import com.midcores.silapan.domain.repository.PengaduanRepository

class GetPengaduanListUseCase(private val repo: PengaduanRepository) {
    suspend operator fun invoke(refresh: Boolean = false) =
        repo.getAll(refresh)
}