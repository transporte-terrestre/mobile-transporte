package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.repository.DocumentInspectionRepository
import org.rol.transportation.utils.Resource

class GetDocumentInspectionUseCase(private val repository: DocumentInspectionRepository) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<DocumentInspection>> {
        return repository.getDocumentInspection(vehiculoId, documentId)
    }
}