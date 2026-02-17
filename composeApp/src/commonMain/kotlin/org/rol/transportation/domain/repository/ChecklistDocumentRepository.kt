package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.ChecklistDocument
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.utils.Resource

interface ChecklistDocumentRepository {
    suspend fun getChecklistDocument(
        vehiculoId: Int,
        documentType: ChecklistDocumentType,
        documentId: Int?
    ): Flow<Resource<ChecklistDocument>>

    suspend fun upsertChecklistDocument(
        vehiculoId: Int,
        viajeId: Int,
        documentType: ChecklistDocumentType,
        viajeTipo: TripType,
        photoUrl: String
    ): Flow<Resource<ChecklistDocument>>
}