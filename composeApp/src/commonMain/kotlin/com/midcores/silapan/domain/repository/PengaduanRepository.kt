package com.midcores.silapan.domain.repository

import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.domain.model.Pengaduan
import com.midcores.silapan.presentation.viewmodel.pengaduan.FormUiState

interface PengaduanRepository {
    suspend fun getAll(forceRefresh: Boolean = false): List<Pengaduan>?
    suspend fun create(dto: CreatePengaduanDto): FormUiState
    suspend fun update(id: String, pengaduan: CreatePengaduanDto): FormUiState
    suspend fun delete(id: String)
}