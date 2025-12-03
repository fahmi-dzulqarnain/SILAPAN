package com.midcores.silapan.api

object ApiRoutes {
    private const val URL = "https://webgear.my.id"
    private const val BASE_URL = "${URL}/api/v1"
    const val STORAGE = "${URL}/storage/"
    const val PENGADUAN = "$BASE_URL/pengaduan"
    const val GALLERY = "$BASE_URL/gallery"
    const val JADWAL = "$BASE_URL/jadwal"
    const val PROFILE = "$BASE_URL/profil"
    const val LOGIN = "$BASE_URL/login"
    const val LOGOUT = "$BASE_URL/logout"
}