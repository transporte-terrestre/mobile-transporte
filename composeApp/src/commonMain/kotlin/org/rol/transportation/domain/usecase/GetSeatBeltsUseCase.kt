package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.repository.SeatBeltsRepository

class GetSeatBeltsUseCase(private val repository: SeatBeltsRepository) {
    suspend operator fun invoke(vehiculoId: Int, documentId: Int?) = repository.getSeatBelts(vehiculoId, documentId)
}