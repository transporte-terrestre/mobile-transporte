
package org.rol.transportation.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ImageCompressor {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun compress(imageBytes: ByteArray, quality: Int, maxWidth: Int): ByteArray {
        return try {
            val data = imageBytes.usePinned { pinned ->
                NSData.dataWithBytes(pinned.addressOf(0), imageBytes.size.toULong())
            }

            val image = UIImage(data = data) ?: return imageBytes

            val jpegData = UIImageJPEGRepresentation(image, quality / 100.0) ?: return imageBytes

            ByteArray(jpegData.length.toInt()).apply {
                usePinned { pinned ->
                    memcpy(pinned.addressOf(0), jpegData.bytes, jpegData.length)
                }
            }
        } catch (e: Exception) {
            imageBytes
        }
    }
}