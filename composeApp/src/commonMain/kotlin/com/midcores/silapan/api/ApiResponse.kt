package com.midcores.silapan.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String?,
    val message: String?,
    val count: Int? = null,
    val data: T? = null
)