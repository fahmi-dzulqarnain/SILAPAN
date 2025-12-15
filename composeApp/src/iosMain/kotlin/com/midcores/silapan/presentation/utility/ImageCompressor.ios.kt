package com.midcores.silapan.presentation.utility

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.memcpy

actual object ImageCompressor {
    @OptIn(ExperimentalForeignApi::class)
    actual fun compress(imageBytes: ByteArray, quality: Int): ByteArray {
        // 1. Convert Kotlin ByteArray to NSData
        val data = imageBytes.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), imageBytes.size.toULong())
        }

        // 2. Create UIImage and compress to JPEG
        val uiImage = UIImage(data = data)
        val jpegData = UIImageJPEGRepresentation(uiImage, quality / 100.0)
            ?: return imageBytes

        // 3. Convert back to Kotlin ByteArray
        return ByteArray(jpegData.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), jpegData.bytes, jpegData.length)
            }
        }
    }
}