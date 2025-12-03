package com.midcores.silapan.data.repository

import com.midcores.silapan.api.service.JadwalPelayananService
import com.midcores.silapan.database.DatabaseDataSource
import com.midcores.silapan.domain.model.JadwalPelayanan
import com.midcores.silapan.domain.repository.JadwalRepository

class JadwalRepositoryImpl(
    private val db: DatabaseDataSource,
    private val api: JadwalPelayananService
) : JadwalRepository {

    override suspend fun getLatest(
        year: String,
        forceRefresh: Boolean
    ): List<JadwalPelayanan> {
        val cached = db.getLatestJadwal(year)

        if (cached.isNotEmpty() && !forceRefresh) {
            return cached.map {
                JadwalPelayanan(
                    it.id, it.bulan, it.tahun,
                    it.file_path,
                    it.created_at, it.updated_at
                )
            }
        }

        val remote = api.getLatestJadwal(year).data

        return remote?.map {
            JadwalPelayanan(
                it.id, it.bulan, it.tahun,
                it.filePath,
                it.createdAt, it.updatedAt
            )
        } ?: listOf()
    }
}