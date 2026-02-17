

package org.rol.transportation.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import kotlin.math.min
import androidx.core.graphics.scale

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ImageCompressor {
    actual suspend fun compress(imageBytes: ByteArray, quality: Int, maxWidth: Int): ByteArray {
        return try {
            // Decodificar
            val originalBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) ?: return imageBytes

            // Calcular escala manteniendo ratio
            val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
            val width = min(originalBitmap.width, maxWidth)
            val height = (width / aspectRatio).toInt()

            // edimensionar si es necesario
            val scaledBitmap = if (originalBitmap.width > maxWidth) {
                originalBitmap.scale(width, height)
            } else {
                originalBitmap
            }

            // Comprimir a JPEG
            val stream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            stream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            imageBytes
        }
    }
}