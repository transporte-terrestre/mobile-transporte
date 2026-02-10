package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.StorageApi
import org.rol.transportation.data.remote.dto.storage.StorageUploadResponseDto
import org.rol.transportation.domain.model.StorageUploadResponse
import org.rol.transportation.utils.Resource

class StorageRepositoryImpl(
    private val storageApi: StorageApi
) : StorageRepository {

    override suspend fun uploadImage(
        imageBytes: ByteArray,
        fileName: String
    ): Flow<Resource<StorageUploadResponse>> = flow {
        try {
            emit(Resource.Loading)
            val response = storageApi.uploadImage(imageBytes, fileName)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al subir la imagen"))
        }
    }

    private fun StorageUploadResponseDto.toDomain() = StorageUploadResponse(
        publicId = publicId,
        url = url,
        secureUrl = secureUrl,
        format = format,
        width = width,
        height = height,
        bytes = bytes,
        resourceType = resourceType,
        createdAt = createdAt
    )
}