package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.StorageUploadResponse
import org.rol.transportation.utils.Resource

interface StorageRepository {
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Flow<Resource<StorageUploadResponse>>
}