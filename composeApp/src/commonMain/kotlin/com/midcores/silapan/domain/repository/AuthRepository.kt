package com.midcores.silapan.domain.repository

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Boolean

    suspend fun logout(): Boolean
}