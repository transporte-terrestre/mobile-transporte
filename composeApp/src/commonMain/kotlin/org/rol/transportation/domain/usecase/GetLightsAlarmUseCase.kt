package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.lights_alarm.LightsAlarmInspection
import org.rol.transportation.domain.repository.LightsAlarmRepository
import org.rol.transportation.utils.Resource

class GetLightsAlarmUseCase(private val repository: LightsAlarmRepository) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<LightsAlarmInspection>> {
        return repository.getLightsAlarm(vehiculoId, documentId)
    }
}