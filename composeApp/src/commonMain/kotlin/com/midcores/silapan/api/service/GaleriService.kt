package com.midcores.silapan.api.service

import com.midcores.silapan.api.ApiResponse
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.api.model.GaleriDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText

class GaleriService(private val client: HttpClient) {

    suspend fun getAll(): ApiResponse<List<GaleriDto>>? {
        return try {
            val response: ApiResponse<List<GaleriDto>> = client
                .get(ApiRoutes.GALLERY)
                .body()
            response
        } catch (e: Exception) {
            null
        }
    }
}