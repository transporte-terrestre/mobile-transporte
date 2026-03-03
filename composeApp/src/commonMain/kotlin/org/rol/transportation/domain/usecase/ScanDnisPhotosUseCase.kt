package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.data.remote.dto.passenger.ScanDnisResponse
import org.rol.transportation.domain.repository.PassengerRepository
import org.rol.transportation.utils.Resource

class ScanDnisPhotosUseCase(private val repository: PassengerRepository) {
    suspend operator fun invoke(tripId: Int, viajeTramoId: Int, urls: List<String>): Flow<Resource<ScanDnisResponse>> {
        return repository.scanDnis(tripId, viajeTramoId, urls)
    }
}
