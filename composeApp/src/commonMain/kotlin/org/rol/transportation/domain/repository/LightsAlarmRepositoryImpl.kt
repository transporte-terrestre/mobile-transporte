package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.LightsAlarmApi
import org.rol.transportation.data.remote.dto.lights_alarm.LightItemDto
import org.rol.transportation.data.remote.dto.lights_alarm.LightsAlarmDto
import org.rol.transportation.data.remote.dto.lights_alarm.UpsertLightItemDto
import org.rol.transportation.data.remote.dto.lights_alarm.UpsertLightsAlarmRequest
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.lights_alarm.LightItem
import org.rol.transportation.domain.model.lights_alarm.LightsAlarmInspection
import org.rol.transportation.utils.Resource

class LightsAlarmRepositoryImpl(private val api: LightsAlarmApi) : LightsAlarmRepository {

    override suspend fun getLightsAlarm(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<LightsAlarmInspection>> = flow {
        try {
            emit(Resource.Loading)

            val dto = api.getLightsAlarm(vehiculoId, documentId)

            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("El servidor no devolvió la estructura de luces y alarmas."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener luces y alarmas"))
        }
    }

    override suspend fun upsertLightsAlarm(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: LightsAlarmInspection
    ): Flow<Resource<LightsAlarmInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertLightsAlarm(vehiculoId, viajeId, viajeTipo.value, request)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar luces y alarmas"))
        }
    }

    // --- MAPPERS ---

    private fun LightsAlarmDto.toDomain() = LightsAlarmInspection(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        items = document.mapValues { it.value.toDomain() }
    )

    private fun LightItemDto.toDomain() = LightItem(
        label = label,
        estado = estado,
        observacion = observacion ?: ""
    )

    private fun LightsAlarmInspection.toUpsertRequest(): UpsertLightsAlarmRequest {
        val i = items

        // Helper para mapear item a DTO de Upsert (SIN LABEL)
        fun map(key: String): UpsertLightItemDto {
            val item = i[key]
            return UpsertLightItemDto(
                estado = item?.estado ?: false,
                observacion = if (item?.observacion.isNullOrBlank()) null else item.observacion
            )
        }

        return UpsertLightsAlarmRequest(
            alarmaRetroceso = map("alarmaRetroceso"),
            alarmaCinturon = map("alarmaCinturon"),
            claxon = map("claxon"),
            lucesCabina = map("lucesCabina"),
            lucesSalon = map("lucesSalon"),
            lucesAltasDerecho = map("lucesAltasDerecho"),
            lucesAltasIzquierdo = map("lucesAltasIzquierdo"),
            lucesBajasDerecho = map("lucesBajasDerecho"),
            lucesBajasIzquierdo = map("lucesBajasIzquierdo"),
            lucesLateralesDerecho = map("lucesLateralesDerecho"),
            lucesLateralesIzquierdo = map("lucesLateralesIzquierdo"),
            lucesNeblineros = map("lucesNeblineros"),
            lucesEstacionamientoDerecho = map("lucesEstacionamientoDerecho"),
            lucesEstacionamientoIzquierdo = map("lucesEstacionamientoIzquierdo"),
            lucesDireccionalesDerecho = map("lucesDireccionalesDerecho"),
            lucesDireccionalesIzquierdo = map("lucesDireccionalesIzquierdo"),
            luzEstroboscopica = map("luzEstroboscopica"),
            luzPertiga = map("luzPertiga"),
            pruebaRadio = map("pruebaRadio"),
            botonPanico = map("botonPanico")
        )
    }

}