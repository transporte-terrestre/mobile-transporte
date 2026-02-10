package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.ChecklistDocument
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.repository.ChecklistDocumentRepository
import org.rol.transportation.utils.Resource

class UpsertChecklistDocumentUseCase(
    private val repository: ChecklistDocumentRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        viajeId: Int,
        documentType: ChecklistDocumentType,
        viajeTipo: TripType,
        photoUrl: String
    ): Flow<Resource<ChecklistDocument>> {
        return repository.upsertChecklistDocument(vehiculoId, viajeId, documentType, viajeTipo, photoUrl)
    }
}