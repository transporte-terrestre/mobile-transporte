package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.repository.DocumentInspectionRepository
import org.rol.transportation.utils.Resource

class UpsertDocumentInspectionUseCase(private val repository: DocumentInspectionRepository) {
    suspend operator fun invoke(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: DocumentInspection
    ): Flow<Resource<DocumentInspection>> {
        return repository.upsertDocumentInspection(vehiculoId, viajeId, viajeTipo, inspection)
    }
}