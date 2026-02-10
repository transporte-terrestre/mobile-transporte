package org.rol.transportation.domain.usecase

import org.rol.transportation.data.remote.dto.trip_services.CreateSegmentRequest
import org.rol.transportation.domain.repository.TripServicesRepository

class CreateSegmentUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(tripId: Int, request: CreateSegmentRequest) = repository.createSegment(tripId, request)
}