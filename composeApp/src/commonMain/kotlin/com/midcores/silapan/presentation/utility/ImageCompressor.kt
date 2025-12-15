package com.midcores.silapan.presentation.utility

expect object ImageCompressor {
    fun compress(imageBytes: ByteArray, quality: Int = 80): ByteArray
}