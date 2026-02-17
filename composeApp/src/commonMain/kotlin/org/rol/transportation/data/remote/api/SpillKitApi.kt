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
import kotlinx.serialization.json.JsonObject
import org.rol.transportation.data.remote.dto.spill_kit.SpillKitDto
import org.rol.transportation.utils.Constants


class SpillKitApi(private val client: HttpClient) {

    suspend fun getSpillKit(
        vehiculoId: Int,
        documentId: Int? = null
    ): SpillKitDto? {
        val response = client.get("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/kit-antiderrames/find") {
            documentId?.let { parameter("documentId", it) }
            expectSuccess = false
        }
        return when (response.status.value) {
            200 -> response.body<SpillKitDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}")
        }
    }

    suspend fun upsertSpillKit(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: JsonObject
    ): SpillKitDto {
        return client.post("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/kit-antiderrames/upsert") {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}