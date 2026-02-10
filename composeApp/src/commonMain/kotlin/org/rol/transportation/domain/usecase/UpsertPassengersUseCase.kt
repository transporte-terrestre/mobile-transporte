package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.model.Passenger
import org.rol.transportation.domain.repository.PassengerRepository

class UpsertPassengersUseCase(private val repository: PassengerRepository) {
    suspend operator fun invoke(tripId: Int, passengers: List<Passenger>) = repository.upsertPassengers(tripId, passengers)
}