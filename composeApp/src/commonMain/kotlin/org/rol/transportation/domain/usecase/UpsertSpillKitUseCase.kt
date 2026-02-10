package org.rol.transportation.domain.usecase

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.spill_kit.SpillKitInspection
import org.rol.transportation.domain.repository.SpillKitRepository

class UpsertSpillKitUseCase(private val repository: SpillKitRepository) {
    suspend operator fun invoke(vehiculoId: Int, viajeId: Int, viajeTipo: TripType, inspection: SpillKitInspection) = repository.upsertSpillKit(vehiculoId, viajeId, viajeTipo, inspection)
}