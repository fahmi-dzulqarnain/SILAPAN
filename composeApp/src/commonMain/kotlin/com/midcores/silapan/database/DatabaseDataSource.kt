package com.midcores.silapan.database

import com.midcores.silapan.db.Galeri
import com.midcores.silapan.db.Galeri_image
import com.midcores.silapan.db.Pengaduan
import com.midcores.silapan.db.SilapanDatabase
import com.midcores.silapan.domain.model.GaleriImage
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanFilter

class DatabaseDataSource(private val database: SilapanDatabase) {

    private val pengaduanQueries get() = database.pengaduanQueries
    private val galeriQueries get() = database.galeriQueries
    private val galeriImageQueries get() = database.galeriImageQueries
    private val jadwalQueries get() = database.jadwalPelayananQueries
    private val profileQueries get() = database.profileQueries

    // Pengaduan
    fun upsertPengaduan(item: Pengaduan) = pengaduanQueries.upsertAllFieldPengaduan(item)
    fun getAllPengaduan(): List<Pengaduan> = pengaduanQueries.selectAll().executeAsList()
    fun getPengaduanByJenis(jenis: PengaduanFilter): List<Pengaduan> =
        pengaduanQueries.selectAllByJenis(jenis.label).executeAsList()

    fun deletePengaduan(id: String) = pengaduanQueries.deleteById(id)
    fun deleteAllPengaduan() = pengaduanQueries.deleteAll()

    // Gallery
    fun getAllGallery(): List<Galeri> = galeriQueries.selectAll().executeAsList()
    fun getImagesByGaleriId(id: String): List<Galeri_image> = galeriImageQueries
        .selectImagesByGaleriId(id)
        .executeAsList()
    fun upsertGallery(item: Galeri) = galeriQueries.upsertGaleri(
        item.id, item.title, item.description,
        item.feature_image, item.category,
        item.created_at
    )
    fun upsertGalleryImages(item: GaleriImage, galeriId: String) = galeriImageQueries.upsertGaleriImage(
        item.id, galeriId, item.imageURL, item.createdAt
    )

    // Jadwal
    fun getLatestJadwal(year: String) = jadwalQueries.selectByYear(year).executeAsList()

    // Profile
    fun getProfileByType(type: String) = profileQueries.selectByType(type).executeAsOneOrNull()
}