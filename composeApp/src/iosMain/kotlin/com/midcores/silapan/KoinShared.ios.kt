package com.midcores.silapan

import com.midcores.silapan.api.HttpClientFactory
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsImplementation::class)
actual val platformModule: Module
    get() = module {
        factory<Settings>(named("encrypted")) {
            KeychainSettings("encrypted")
        }
        factory<Settings>(named("plaintext")) {
            NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
        }
        factory<HttpClient> {
            HttpClientFactory(get()).create(Darwin)
        }
    }