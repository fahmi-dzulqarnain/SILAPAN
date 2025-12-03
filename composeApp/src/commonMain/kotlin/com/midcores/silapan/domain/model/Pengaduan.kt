package com.midcores.silapan.domain.model

import com.midcores.silapan.db.Pengaduan
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

data class Pengaduan(
    val id: String,
    val title: String,
    val description: String?,
    val photo: String?,
    val afterPhoto: String?,
    val addressLabel: String?,
    val location: String?,
    val jenis: String,
    val status: String?,
    val createdAt: String
) {
    constructor(pengaduan: Pengaduan) : this(
        pengaduan.id,
        pengaduan.title,
        pengaduan.description,
        pengaduan.photo,
        pengaduan.after_photo,
        pengaduan.address_label,
        pengaduan.location,
        pengaduan.jenis,
        pengaduan.status,
        pengaduan.created_at,
    )

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun displayableCreatedAt(): String {
        val inputFormat = LocalDateTime.Format {
            byUnicodePattern("yyyy-MM-dd HH:mm:ss")
        }
        val dateTime: LocalDateTime = inputFormat.parse(createdAt)

        val outputFormat = LocalDateTime.Format {
            byUnicodePattern("dd/MM/yyyy HH:mm")
        }

        return dateTime.format(outputFormat)
    }
}