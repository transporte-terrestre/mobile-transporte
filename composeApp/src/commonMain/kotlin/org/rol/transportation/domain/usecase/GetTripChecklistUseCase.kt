package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.TripChecklist
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.domain.repository.ChecklistRepository
import org.rol.transportation.utils.Resource

class GetTripChecklistUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(tripId: Int, tipo: ChecklistType): Flow<Resource<TripChecklist>> {
        return checklistRepository.getTripChecklist(tripId, tipo)
    }
}