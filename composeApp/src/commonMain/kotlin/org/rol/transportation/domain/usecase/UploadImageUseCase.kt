package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.StorageUploadResponse
import org.rol.transportation.domain.repository.StorageRepository
import org.rol.transportation.utils.Resource

class UploadImageUseCase(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        fileName: String
    ): Flow<Resource<StorageUploadResponse>> {
        return repository.uploadImage(imageBytes, fileName)
    }
}