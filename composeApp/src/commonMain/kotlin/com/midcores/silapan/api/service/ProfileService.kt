package com.midcores.silapan.api.service

import com.midcores.silapan.api.ApiResponse
import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.api.model.AppProfileDto
import com.midcores.silapan.api.model.ProfileDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ProfileService(private val client: HttpClient) {

    suspend fun getSilapanProfile(): ApiResponse<AppProfileDto> {
        try {
            return client.get("${ApiRoutes.PROFILE}/app").body()
        } catch (e: Exception) {
            print(e.message)
            throw e
        }
    }

    suspend fun getUserProfile(): ProfileDto =
        client.get("${ApiRoutes.PROFILE}/user").body()
}