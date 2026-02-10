package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.lights_alarm.LightsAlarmInspection
import org.rol.transportation.domain.repository.LightsAlarmRepository
import org.rol.transportation.utils.Resource

class UpsertLightsAlarmUseCase(private val repository: LightsAlarmRepository) {
    suspend operator fun invoke(vehiculoId: Int, viajeId: Int, viajeTipo: TripType, inspection: LightsAlarmInspection): Flow<Resource<LightsAlarmInspection>> {
        return repository.upsertLightsAlarm(vehiculoId, viajeId, viajeTipo, inspection)
    }
}