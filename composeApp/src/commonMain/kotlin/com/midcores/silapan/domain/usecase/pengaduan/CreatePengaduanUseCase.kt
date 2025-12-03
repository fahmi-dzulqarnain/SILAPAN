package com.midcores.silapan.domain.usecase.pengaduan

import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.domain.repository.PengaduanRepository

class CreatePengaduanUseCase(private val repo: PengaduanRepository) {
    suspend operator fun invoke(pengaduan: CreatePengaduanDto) = repo.create(pengaduan)
}