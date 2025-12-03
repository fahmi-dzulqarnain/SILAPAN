package com.midcores.silapan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform