package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.utils.Resource

interface DocumentInspectionRepository {
    suspend fun getDocumentInspection(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<DocumentInspection>>

    suspend fun upsertDocumentInspection(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: DocumentInspection
    ): Flow<Resource<DocumentInspection>>
}