package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.first_aid.FirstAidInspection
import org.rol.transportation.domain.repository.FirstAidRepository
import org.rol.transportation.utils.Resource

class UpsertFirstAidUseCase(private val repository: FirstAidRepository) {
    suspend operator fun invoke(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: FirstAidInspection
    ): Flow<Resource<FirstAidInspection>> {
        return repository.upsertFirstAid(vehiculoId, viajeId, viajeTipo, inspection)
    }
}
