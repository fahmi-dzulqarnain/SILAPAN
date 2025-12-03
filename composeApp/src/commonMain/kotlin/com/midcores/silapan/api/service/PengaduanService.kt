package com.midcores.silapan.api.service

import com.midcores.silapan.api.ApiResponse
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.api.model.CreatePengaduanDto
import com.midcores.silapan.api.model.PengaduanDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class PengaduanService(private val client: HttpClient) {

    suspend fun getPengaduanByUser(): ApiResponse<List<PengaduanDto>> {
        return client.get(ApiRoutes.PENGADUAN).body()
    }

    suspend fun createPengaduan(payload: CreatePengaduanDto): ApiResponse<PengaduanDto> {
        return client.post(ApiRoutes.PENGADUAN) {
            setBody(pengaduanDtoBody(payload))
        }.body()
    }

    suspend fun updatePengaduan(id: String, payload: CreatePengaduanDto): ApiResponse<PengaduanDto> {
        return client.put("${ApiRoutes.PENGADUAN}/$id") {
            setBody(pengaduanDtoBody(payload))
        }.body()
    }

    private fun pengaduanDtoBody(payload: CreatePengaduanDto): MultiPartFormDataContent {
        return MultiPartFormDataContent(
            formData {
                append("title", payload.title)
                append("jenis", payload.jenis)

                payload.description?.let {
                    append("description", it)
                }
                payload.addressLabel?.let {
                    append("address_label", it)
                }
                payload.location?.let {
                    append("location", it)
                }
                payload.photoBytes?.let {
                    append("photo", it, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=\"${payload.photoFileName}\""
                        )
                    })
                }
            })
    }

    suspend fun deletePengaduan(id: String): ApiResponse<Unit> {
        return client.delete("${ApiRoutes.PENGADUAN}/$id").body()
    }
}