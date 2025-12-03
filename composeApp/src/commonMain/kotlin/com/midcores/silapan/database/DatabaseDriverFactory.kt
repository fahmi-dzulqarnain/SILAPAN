package com.midcores.silapan.database

import app.cash.sqldelight.db.SqlDriver
import com.midcores.silapan.db.SilapanDatabase

expect fun createDriver(): SqlDriver

fun createDatabase(driver: SqlDriver): SilapanDatabase {
    return SilapanDatabase(driver)
}