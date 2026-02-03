package org.rol.transportation.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.repository.TripRepository

class GetTripsPagedUseCase (
    private val tripRepository: TripRepository
) {
    operator fun invoke(): Flow<PagingData<Trip>> {
        return tripRepository.getTripsPaged()
    }
}