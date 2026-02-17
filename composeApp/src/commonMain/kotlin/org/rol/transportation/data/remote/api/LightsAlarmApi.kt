package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.lights_alarm.LightsAlarmDto
import org.rol.transportation.data.remote.dto.lights_alarm.UpsertLightsAlarmRequest
import org.rol.transportation.utils.Constants

class LightsAlarmApi(private val client: HttpClient) {

    suspend fun getLightsAlarm(
        vehiculoId: Int,
        documentId: Int? = null
    ): LightsAlarmDto? {
        val response = client.get(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/luces-emergencia-alarmas/find"
        ) {
            documentId?.let { parameter("documentId", it) }
            expectSuccess = false
        }

        return when (response.status.value) {
            200 -> response.body<LightsAlarmDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}")
        }
    }

    suspend fun upsertLightsAlarm(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: UpsertLightsAlarmRequest
    ): LightsAlarmDto {
        return client.post(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/luces-emergencia-alarmas/upsert"
        ) {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}