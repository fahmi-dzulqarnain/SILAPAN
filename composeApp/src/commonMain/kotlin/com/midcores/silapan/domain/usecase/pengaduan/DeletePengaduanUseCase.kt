package com.midcores.silapan.domain.usecase.pengaduan

import com.midcores.silapan.domain.repository.PengaduanRepository

class DeletePengaduanUseCase(private val repository: PengaduanRepository) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}