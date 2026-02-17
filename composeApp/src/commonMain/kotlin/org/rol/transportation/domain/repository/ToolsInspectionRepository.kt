package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.tools_inspection.ToolsInspection
import org.rol.transportation.utils.Resource

interface ToolsInspectionRepository {
    suspend fun getTools(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<ToolsInspection>>

    suspend fun upsertTools(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: ToolsInspection
    ): Flow<Resource<ToolsInspection>>
}