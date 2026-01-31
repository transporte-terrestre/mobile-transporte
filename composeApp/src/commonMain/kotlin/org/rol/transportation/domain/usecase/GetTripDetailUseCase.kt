package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.repository.TripRepository
import org.rol.transportation.utils.Resource

class GetTripDetailUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int): Flow<Resource<Trip>> {
        return tripRepository.getTripById(tripId)
    }
}