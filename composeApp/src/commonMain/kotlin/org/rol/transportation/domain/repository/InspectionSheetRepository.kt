package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheet
import org.rol.transportation.utils.Resource

interface InspectionSheetRepository {
    suspend fun getInspectionSheet(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<InspectionSheet>>


    suspend fun upsertInspectionSheet(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        hojaInspeccion: InspectionSheet
    ): Flow<Resource<InspectionSheet>>
}