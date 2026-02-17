package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.lights_alarm.LightsAlarmInspection
import org.rol.transportation.utils.Resource

interface LightsAlarmRepository {
    suspend fun getLightsAlarm(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<LightsAlarmInspection>>

    suspend fun upsertLightsAlarm(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: LightsAlarmInspection
    ): Flow<Resource<LightsAlarmInspection>>
}