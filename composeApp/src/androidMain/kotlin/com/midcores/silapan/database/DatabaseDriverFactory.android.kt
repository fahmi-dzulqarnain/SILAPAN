package com.midcores.silapan.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.midcores.silapan.db.SilapanDatabase
import org.koin.java.KoinJavaComponent.get

actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
    SilapanDatabase.Schema,
    context = get(Context::class.java),
    "silapan.db"
)
