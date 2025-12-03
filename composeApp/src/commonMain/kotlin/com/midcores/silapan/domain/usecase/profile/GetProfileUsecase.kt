package com.midcores.silapan.domain.usecase.profile

import com.midcores.silapan.domain.repository.ProfileRepository

class GetUserProfileUsecase(private val repo: ProfileRepository) {
    suspend operator fun invoke(isForceRefresh: Boolean) = repo.getUser(isForceRefresh)
}

class GetSilapanProfileUsecase(private val repo: ProfileRepository) {
    suspend operator fun invoke(isForceRefresh: Boolean) = repo.getSilapan(isForceRefresh)
}