package com.midcores.silapan.settings

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class EncryptedSettingsFactory(private val context: Context) : Settings.Factory {
    val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    override fun create(name: String?): Settings {
        val preferencesName = name ?: "${context.packageName}_preferences"

        return SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                context,
                preferencesName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        )
    }
}