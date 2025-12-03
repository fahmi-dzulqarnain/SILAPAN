package com.midcores.silapan.domain.usecase.auth

import com.midcores.silapan.domain.repository.AuthRepository

class LogoutUsecase(private val repo: AuthRepository) {
    suspend operator fun invoke() : Boolean {
        return repo.logout()
    }
}
