package com.midcores.silapan.presentation.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

actual object ImageCompressor {
    actual fun compress(imageBytes: ByteArray, quality: Int): ByteArray {
        // 1. Decode the raw bytes into a Bitmap
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ?: return imageBytes // Return original if decoding fails

        // 2. Compress to JPEG
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        // 3. Clean up memory immediately
        bitmap.recycle()

        return outputStream.toByteArray()
    }
}