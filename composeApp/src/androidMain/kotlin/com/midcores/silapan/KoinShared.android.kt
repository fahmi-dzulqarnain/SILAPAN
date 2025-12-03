package com.midcores.silapan

import android.content.Context
import com.midcores.silapan.settings.EncryptedSettingsFactory
import com.midcores.silapan.api.HttpClientFactory
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        factory<Settings>(named("encrypted")) {
            val settingsFactory = EncryptedSettingsFactory(androidContext())
            settingsFactory.create("encrypted")
        }
        factory<Settings>(named("plaintext")) {
            val preferences = androidContext().getSharedPreferences(
                "plaintext",
                Context.MODE_PRIVATE
            )

            SharedPreferencesSettings(preferences)
        }
        factory<HttpClient> {
            HttpClientFactory(get()).create(OkHttp)
        }
    }