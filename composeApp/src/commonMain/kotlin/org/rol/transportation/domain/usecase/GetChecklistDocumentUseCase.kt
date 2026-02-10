package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.ChecklistDocument
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.repository.ChecklistDocumentRepository
import org.rol.transportation.utils.Resource

class GetChecklistDocumentUseCase(
    private val repository: ChecklistDocumentRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentType: ChecklistDocumentType,
        documentId: Int? = null
    ): Flow<Resource<ChecklistDocument>> {
        return repository.getChecklistDocument(vehiculoId, documentType, documentId)
    }
}