package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Passenger
import org.rol.transportation.utils.Resource

interface PassengerRepository {
    suspend fun getPassengers(tripId: Int): Flow<Resource<List<Passenger>>>
    suspend fun upsertPassengers(tripId: Int, passengers: List<Passenger>): Flow<Resource<List<Passenger>>>
}