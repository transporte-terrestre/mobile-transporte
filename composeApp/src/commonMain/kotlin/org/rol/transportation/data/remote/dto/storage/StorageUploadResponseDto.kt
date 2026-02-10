package org.rol.transportation.data.remote.dto.storage

import kotlinx.serialization.Serializable

@Serializable
data class StorageUploadResponseDto(
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