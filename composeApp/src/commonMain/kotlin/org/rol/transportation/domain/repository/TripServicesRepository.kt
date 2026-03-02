package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest
import org.rol.transportation.utils.Resource

interface TripServicesRepository {
    suspend fun getTripSegments(tripId: Int): Flow<Resource<List<SegmentDto>>>
    suspend fun getNextStep(tripId: Int, tipo: String? = null): Flow<Resource<NextStepDto>>
    suspend fun registerDeparture(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>>
    suspend fun registerArrival(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>>
    suspend fun registerCheckpoint(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>>
    suspend fun registerStop(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>>
    suspend fun registerRest(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>>
    suspend fun updateSegment(segmentId: Int, request: UpdateSegmentRequest): Flow<Resource<SegmentDto>>
    suspend fun deleteSegment(segmentId: Int): Flow<Resource<SegmentDto>>
}