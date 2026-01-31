package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.repository.TripRepository
import org.rol.transportation.utils.Resource

class GetTripsUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        limit: Int = 10,
        search: String? = null
    ): Flow<Resource<Pair<List<Trip>, Int>>> {
        return tripRepository.getTrips(page, limit, search)
    }
}