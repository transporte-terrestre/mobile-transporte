package org.rol.transportation.domain.usecase

import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.domain.repository.TripServicesRepository

class RegisterDepartureUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(tripId: Int, request: RegisterLocationRequest) = repository.registerDeparture(tripId, request)
}
