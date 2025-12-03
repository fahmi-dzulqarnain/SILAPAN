package com.midcores.silapan.domain.repository

import com.midcores.silapan.domain.model.AppProfile
import com.midcores.silapan.domain.model.Profile

interface ProfileRepository {
    suspend fun getSilapan(forceRefresh: Boolean = false): AppProfile?
    suspend fun getUser(forceRefresh: Boolean = false): Profile?
}