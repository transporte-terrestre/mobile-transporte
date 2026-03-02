package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.domain.repository.TripServicesRepository
import org.rol.transportation.utils.Resource

class DeleteSegmentUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(segmentId: Int): Flow<Resource<SegmentDto>> {
        return repository.deleteSegment(segmentId)
    }
}
