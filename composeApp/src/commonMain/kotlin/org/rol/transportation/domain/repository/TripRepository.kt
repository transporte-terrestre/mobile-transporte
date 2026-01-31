package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.utils.Resource

interface TripRepository {
    suspend fun getTrips(page: Int, limit: Int, search: String?): Flow<Resource<Pair<List<Trip>, Int>>>
    suspend fun getTripById(id: Int): Flow<Resource<Trip>>
}