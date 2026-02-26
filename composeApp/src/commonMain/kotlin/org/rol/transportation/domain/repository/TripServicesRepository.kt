package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.ProximoTramoDto
import org.rol.transportation.data.remote.dto.trip_services.RegistrarServicioRequest
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.utils.Resource

interface TripServicesRepository {
    suspend fun getTripSegments(tripId: Int): Flow<Resource<List<SegmentDto>>>
    suspend fun getNextStep(tripId: Int): Flow<Resource<NextStepDto>>
    suspend fun getProximoTramo(tripId: Int): Flow<Resource<ProximoTramoDto>>
    suspend fun registrarServicio(tripId: Int, tipo: String, request: RegistrarServicioRequest): Flow<Resource<SegmentDto>>
}