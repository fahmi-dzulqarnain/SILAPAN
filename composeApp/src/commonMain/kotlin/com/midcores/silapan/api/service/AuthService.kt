package com.midcores.silapan.api.service

import com.midcores.silapan.api.ApiRoutes
import com.midcores.silapan.api.model.auth.LoginRequest
import com.midcores.silapan.api.model.auth.LoginResponse
import com.midcores.silapan.api.model.auth.LogoutResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthService(private val client: HttpClient) {

    suspend fun login(phone: String, password: String): LoginResponse? {
        return try {
            val request = LoginRequest(phone, password)
            val response = client
                .post(ApiRoutes.LOGIN) {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            response.body()
        } catch (e: Exception) {
            println("❌ [API ERROR] ${e::class.simpleName}: ${e.message}")
            null
        }
    }

    suspend fun logout(): LogoutResponse? {
        return try {
            val response = client
                .post(ApiRoutes.LOGOUT) {
                    contentType(ContentType.Application.Json)
                }

            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            println("❌ [API ERROR] ${e::class.simpleName}: ${e.message}")
            null
        }
    }
}