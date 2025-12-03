package com.midcores.silapan.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PengaduanDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val photo: String? = null,
    val location: String? = null,
    val jenis: String,
    val status: String?,

    @SerialName("after_photo") val afterPhoto: String? = null,
    @SerialName("address_label") val addressLabel: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class CreatePengaduanDto(
    val title: String,
    val description: String?,
    val jenis: String,
    val location: String?,

    @SerialName("address_label") val addressLabel: String? = null,
    @SerialName("photo") val photoBytes: ByteArray?,
    @SerialName("photo_file_name") val photoFileName: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreatePengaduanDto

        if (title != other.title) return false
        if (description != other.description) return false
        if (jenis != other.jenis) return false
        if (location != other.location) return false
        if (!photoBytes.contentEquals(other.photoBytes)) return false
        if (photoFileName != other.photoFileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + jenis.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (photoBytes?.contentHashCode() ?: 0)
        result = 31 * result + photoFileName.hashCode()
        return result
    }
}