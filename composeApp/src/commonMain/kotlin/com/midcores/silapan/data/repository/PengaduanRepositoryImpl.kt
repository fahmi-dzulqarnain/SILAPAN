package com.midcores.silapan.data.repository

import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.api.service.PengaduanService
import com.midcores.silapan.database.DatabaseDataSource
import com.midcores.silapan.db.Pengaduan as PengaduanEntity
import com.midcores.silapan.domain.model.Pengaduan
import com.midcores.silapan.domain.repository.PengaduanRepository
import com.midcores.silapan.presentation.viewmodel.pengaduan.FormUiState

class PengaduanRepositoryImpl(
    private val db: DatabaseDataSource,
    private val api: PengaduanService
) : PengaduanRepository {

    override suspend fun getAll(forceRefresh: Boolean): List<Pengaduan>? {
        val cached = db.getAllPengaduan()

        if (cached.isNotEmpty() && !forceRefresh) {
            return cached.map {
                Pengaduan(it)
            }
        }

        if (forceRefresh) {
            db.deleteAllPengaduan()
        }

        val fresh = api.getPengaduanByUser().data
        fresh?.forEach {
            db.upsertPengaduan(
                PengaduanEntity(
                    it.id, it.title, it.description,
                    it.photo, it.afterPhoto,
                    it.addressLabel, it.location,
                    it.jenis, it.status,
                    it.createdAt
                )
            )
        }

        return fresh?.map {
            Pengaduan(
                it.id, it.title, it.description,
                it.photo, it.afterPhoto, it.addressLabel,
                it.location, it.jenis, it.status, it.createdAt
            )
        }
    }

    override suspend fun create(dto: CreatePengaduanDto): FormUiState =
        upsertPengaduan(dto)

    override suspend fun update(id: String, pengaduan: CreatePengaduanDto): FormUiState =
        upsertPengaduan(pengaduan, id)

    private suspend fun upsertPengaduan(
        dto: CreatePengaduanDto,
        id: String? = null
    ): FormUiState {
        try {
            val response =
                if (id == null) api.createPengaduan(dto).data
                else api.updatePengaduan(id, dto).data

            response?.let {
                db.upsertPengaduan(
                    PengaduanEntity(
                        it.id, dto.title, dto.description,
                        it.photo, it.afterPhoto,
                        it.addressLabel, it.location,
                        dto.jenis, it.status,
                        it.createdAt
                    )
                )
            }

            return FormUiState(success = true, loading = false)
        } catch (exception: Exception) {
            return FormUiState(error = exception.message)
        }
    }

    override suspend fun delete(id: String) {
        db.deletePengaduan(id)
        api.deletePengaduan(id)
    }
}