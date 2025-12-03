package com.midcores.silapan.data.repository

import com.midcores.silapan.api.service.AuthService
import com.midcores.silapan.SettingsRepository
import com.midcores.silapan.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val api: AuthService
) : AuthRepository {
    override suspend fun login(
        phoneNumber: String,
        password: String
    ) : Boolean {
        val response = api.login(phoneNumber, password)
        val token = response?.token

        if (token != null) {
            settingsRepository.authToken = token
            return true
        } else {
            return false
        }
    }

    override suspend fun logout(): Boolean {
        val response = api.logout()
        val message = response?.message

        if (message != null) {
            settingsRepository.authToken = ""
            return true
        } else {
            return false
        }
    }
}