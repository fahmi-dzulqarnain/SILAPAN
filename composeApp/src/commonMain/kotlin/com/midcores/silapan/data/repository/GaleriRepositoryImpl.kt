package com.midcores.silapan.data.repository

import com.midcores.silapan.api.ApiResponse
import com.midcores.silapan.api.model.GaleriDto
import com.midcores.silapan.api.service.GaleriService
import com.midcores.silapan.database.DatabaseDataSource
import com.midcores.silapan.domain.model.Galeri
import com.midcores.silapan.db.Galeri as GaleriEntity
import com.midcores.silapan.domain.model.GaleriImage
import com.midcores.silapan.domain.repository.GaleriRepository

class GaleriRepositoryImpl(
    private val db: DatabaseDataSource,
    private val api: GaleriService
) : GaleriRepository {

    override suspend fun getAll(forceRefresh: Boolean): List<Galeri> {
        val local = db.getAllGallery()
        var remote: ApiResponse<List<GaleriDto>>? = null

        if (forceRefresh || local.isEmpty()) {
            remote = api.getAll()

            remote?.data?.forEach { galeri ->
                val item = GaleriEntity(
                    galeri.id, galeri.title,
                    galeri.description, galeri.featureImage,
                    galeri.category, galeri.createdAt
                )

                db.upsertGallery(item)

                galeri.images.map { img ->
                    GaleriImage(img.id, img.imageURL, img.createdAt)
                }.forEach {
                    db.upsertGalleryImages(it, galeri.id)
                }

            }
        }

        if (local.isNotEmpty() && !forceRefresh) {
            return local.map {
                val images = db.getImagesByGaleriId(it.id).map { image ->
                    GaleriImage(
                        image.id, image.image_url,
                        image.created_at
                    )
                }

                Galeri(
                    it.id, it.title, it.description,
                    it.feature_image,
                    it.category, images,
                    it.created_at
                )
            }
        }

        return remote?.data?.map {
            val images = it.images.map { img ->
                GaleriImage(img.id, img.imageURL, img.createdAt)
            }

            Galeri(
                it.id, it.title, it.description,
                it.featureImage, it.category, images,
                it.createdAt
            )
        } ?: listOf(
            Galeri(
                "it.id", "it.title", "it.description",
                "it.featureImage", "it.category", emptyList(),
                "it.createdAt"
            )
        )
    }

    override suspend fun getByCategory(category: String) =
        getAll(false).filter { it.category == category }
}