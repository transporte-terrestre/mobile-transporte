package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.first_aid.FirstAidInspection
import org.rol.transportation.domain.repository.FirstAidRepository
import org.rol.transportation.utils.Resource

class GetFirstAidUseCase(
    private val repository: FirstAidRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<FirstAidInspection>> {
        return repository.getFirstAid(vehiculoId, documentId)
    }
}