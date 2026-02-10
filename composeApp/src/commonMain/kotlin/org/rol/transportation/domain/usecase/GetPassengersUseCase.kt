package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.repository.PassengerRepository
import org.rol.transportation.domain.repository.TripRepository

class GetPassengersUseCase(private val repository: PassengerRepository) {
    suspend operator fun invoke(tripId: Int) = repository.getPassengers(tripId)
}