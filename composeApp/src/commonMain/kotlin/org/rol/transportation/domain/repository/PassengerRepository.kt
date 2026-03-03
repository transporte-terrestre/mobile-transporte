package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.passenger.ScanDnisResponse
import org.rol.transportation.domain.model.Passenger
import org.rol.transportation.utils.Resource

interface PassengerRepository {
    suspend fun getPassengers(tripId: Int, viajeTramoId: Int? = null): Flow<Resource<List<Passenger>>>
    suspend fun scanDnis(tripId: Int, viajeTramoId: Int, urls: List<String>): Flow<Resource<ScanDnisResponse>>
}