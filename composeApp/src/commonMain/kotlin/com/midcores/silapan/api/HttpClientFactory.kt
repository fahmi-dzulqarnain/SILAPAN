package com.midcores.silapan.api

import com.midcores.silapan.SettingsRepository
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import sp.bvantur.inspektify.ktor.InspektifyKtor

class HttpClientFactory(private val settings: SettingsRepository) {

    fun create(engine: HttpClientEngineFactory<*>): HttpClient {
        return HttpClient(engine) {
            install(InspektifyKtor)
            install(authorisationInterceptor(settings = settings))
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                    isLenient = true
                })
            }
        }
    }
}