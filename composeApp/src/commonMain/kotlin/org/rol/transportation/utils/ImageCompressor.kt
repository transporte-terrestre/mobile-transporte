package org.rol.transportation.utils

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object ImageCompressor {
    suspend fun compress(imageBytes: ByteArray, quality: Int = 70, maxWidth: Int = 1024): ByteArray
}