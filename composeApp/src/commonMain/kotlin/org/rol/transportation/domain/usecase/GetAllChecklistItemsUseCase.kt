package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.ChecklistItem
import org.rol.transportation.domain.repository.ChecklistRepository
import org.rol.transportation.utils.Resource

class GetAllChecklistItemsUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<ChecklistItem>>> {
        return checklistRepository.getAllChecklistItems()
    }
}