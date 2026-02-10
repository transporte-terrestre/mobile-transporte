package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltsInspection
import org.rol.transportation.domain.repository.SeatBeltsRepository

class UpsertSeatBeltsUseCase(private val repository: SeatBeltsRepository) {
    suspend operator fun invoke(vehiculoId: Int, viajeId: Int, viajeTipo: TripType, inspection: SeatBeltsInspection) = repository.upsertSeatBelts(vehiculoId, viajeId, viajeTipo, inspection)
}