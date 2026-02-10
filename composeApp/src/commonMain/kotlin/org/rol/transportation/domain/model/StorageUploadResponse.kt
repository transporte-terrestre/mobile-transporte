package org.rol.transportation.domain.model

data class StorageUploadResponse(
    val publicId: String,
    val url: String,
    val secureUrl: String,
    val format: String,
    val width: Int,
    val height: Int,
    val bytes: Int,
    val resourceType: String,
    val createdAt: String
)