package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.repository.SpillKitRepository

class GetSpillKitUseCase(private val repository: SpillKitRepository) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ) = repository.getSpillKit(vehiculoId, documentId)
}