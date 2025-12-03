package com.midcores.silapan.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.midcores.silapan.db.SilapanDatabase

actual fun createDriver(): SqlDriver = NativeSqliteDriver(
    SilapanDatabase.Schema,
    "silapan.db"
)