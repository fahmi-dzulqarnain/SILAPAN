package com.midcores.silapan.domain.usecase.auth

import com.midcores.silapan.domain.repository.AuthRepository

class LoginUsecase(private val repo: AuthRepository) {
    suspend operator fun invoke(
        phoneNumber: String,
        password: String
    ) : Boolean {
        return repo.login(phoneNumber, password)
    }
}
