package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.repository.TripServicesRepository

class GetProximoTramoUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(tripId: Int) = repository.getProximoTramo(tripId)
}
