package com.midcores.silapan

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class SettingsRepository: KoinComponent {
    private val plainTextSettings: Settings by inject(named("plainText"))
    private val encryptedSettings: Settings by inject(named("encrypted"))

    private val _authTokenFlow = MutableStateFlow(
        encryptedSettings.getString("AUTH_TOKEN", "")
    )
    val authTokenFlow: StateFlow<String> = _authTokenFlow

    var authToken: String
        get() = _authTokenFlow.value
        set(value) {
            encryptedSettings.putString("AUTH_TOKEN", value)
            _authTokenFlow.value = value
        }

    fun clear() {
        plainTextSettings.clear()
        encryptedSettings.clear()
    }
}