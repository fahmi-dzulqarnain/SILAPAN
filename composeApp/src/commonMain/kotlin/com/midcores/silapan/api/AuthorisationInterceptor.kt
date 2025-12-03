package com.midcores.silapan.api

import com.midcores.silapan.SettingsRepository
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header

fun authorisationInterceptor(settings: SettingsRepository) = createClientPlugin("AuthInterceptor") {
    val token = settings.authTokenFlow.value

    onRequest { request, _ ->
        token.let {
            request.header("Authorization", "Bearer $it")
        }
    }
}
