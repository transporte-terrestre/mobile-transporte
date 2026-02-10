package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.spill_kit.SpillKitInspection
import org.rol.transportation.utils.Resource

interface SpillKitRepository {
    suspend fun getSpillKit(vehiculoId: Int, documentId: Int?): Flow<Resource<SpillKitInspection>>
    suspend fun upsertSpillKit(vehiculoId: Int, viajeId: Int, viajeTipo: TripType, inspection: SpillKitInspection): Flow<Resource<SpillKitInspection>>
}