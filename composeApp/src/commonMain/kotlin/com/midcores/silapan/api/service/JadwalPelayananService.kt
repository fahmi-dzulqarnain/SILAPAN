package com.midcores.silapan.api.service

import com.midcores.silapan.api.ApiResponse
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.api.model.JadwalPelayananDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class JadwalPelayananService(private val client: HttpClient) {

    suspend fun getLatestJadwal(year: String): ApiResponse<List<JadwalPelayananDto>> =
        client.get("${ApiRoutes.JADWAL}/${year}").body()
}