package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.trip_services.CreateSegmentRequest
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.utils.Resource

interface TripServicesRepository {
    suspend fun getTripSegments(tripId: Int): Flow<Resource<List<SegmentDto>>>
    suspend fun getNextStep(tripId: Int): Flow<Resource<NextStepDto>>
    suspend fun createSegment(tripId: Int, request: CreateSegmentRequest): Flow<Resource<SegmentDto>>
}