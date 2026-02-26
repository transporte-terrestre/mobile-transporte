package org.rol.transportation.domain.usecase

import org.rol.transportation.data.remote.dto.trip_services.RegistrarServicioRequest
import org.rol.transportation.domain.repository.TripServicesRepository

class RegistrarServicioUseCase(private val repository: TripServicesRepository) {
    suspend operator fun invoke(tripId: Int, tipo: String, request: RegistrarServicioRequest) =
        repository.registrarServicio(tripId, tipo, request)
}
