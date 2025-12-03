package com.midcores.silapan.domain.usecase.pengaduan

import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.domain.repository.PengaduanRepository

class EditPengaduanUseCase(private val repo: PengaduanRepository) {
    suspend operator fun invoke(id: String, pengaduan: CreatePengaduanDto) =
        repo.update(id, pengaduan)
}