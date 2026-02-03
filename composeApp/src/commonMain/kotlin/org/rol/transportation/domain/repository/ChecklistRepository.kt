package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.ChecklistItem
import org.rol.transportation.domain.model.TripChecklist
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.utils.Resource

interface ChecklistRepository {
    suspend fun getAllChecklistItems(): Flow<Resource<List<ChecklistItem>>>
    suspend fun getTripChecklist(tripId: Int, tipo: ChecklistType): Flow<Resource<TripChecklist>>

    suspend fun upsertTripChecklist(
        tripId: Int,
        tipo: ChecklistType,
        items: List<Pair<Int, Boolean>>,
        observaciones: String?
    ): Flow<Resource<TripChecklist>>
}