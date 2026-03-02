package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest
import org.rol.transportation.domain.repository.TripServicesRepository
import org.rol.transportation.utils.Resource

class UpdateSegmentUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(segmentId: Int, request: UpdateSegmentRequest): Flow<Resource<SegmentDto>> {
        return repository.updateSegment(segmentId, request)
    }
}
