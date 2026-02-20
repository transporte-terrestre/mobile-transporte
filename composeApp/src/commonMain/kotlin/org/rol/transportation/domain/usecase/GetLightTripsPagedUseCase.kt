package org.rol.transportation.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.TripLight
import org.rol.transportation.domain.repository.TripRepository

class GetLightTripsPagedUseCase(private val repository: TripRepository) {
    operator fun invoke(): Flow<PagingData<TripLight>> {
        return repository.getLightTripsPaged()
    }
}
