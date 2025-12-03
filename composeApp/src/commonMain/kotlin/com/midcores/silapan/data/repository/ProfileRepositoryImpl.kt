package com.midcores.silapan.data.repository

import com.midcores.silapan.api.service.ProfileService
import com.midcores.silapan.database.DatabaseDataSource
import com.midcores.silapan.domain.model.AppProfile
import com.midcores.silapan.domain.model.Profile
import com.midcores.silapan.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val db: DatabaseDataSource,
    private val api: ProfileService
) : ProfileRepository {

    override suspend fun getSilapan(forceRefresh: Boolean): AppProfile {
//        val cached = db.getProfileByType("app")
//
//        if (cached != null && !forceRefresh) {
//            return AppProfile(
//                cached.id, cached.description, cached,
//                cached.email, cached.phone, cached.address,
//                cached.description, cached.photo,
//                cached.updated_at
//            )
//        }

        val remote = api.getSilapanProfile().data
        // TODO: save to db

        return AppProfile(
            remote?.title ?: "SILAPAN",
            remote?.description, remote?.contactInfo,
            remote?.location
        )
    }

    override suspend fun getUser(forceRefresh: Boolean): Profile {
        val cached = db.getProfileByType("user")

        if (cached != null && !forceRefresh) {
            return Profile(
                cached.id, cached.type, cached.name,
                cached.email, cached.phone, cached.address,
                cached.description, cached.photo,
                cached.updated_at
            )
        }

        val remote = api.getUserProfile()
        // TODO: save to db

        return Profile(
            remote.id, remote.type, remote.name,
            remote.email, remote.phone, remote.address,
            remote.description, remote.photo,
            remote.updatedAt
        )
    }
}