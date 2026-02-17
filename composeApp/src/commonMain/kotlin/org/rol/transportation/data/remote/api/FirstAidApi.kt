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
import org.rol.transportation.data.remote.dto.first_aid.FirstAidDto
import org.rol.transportation.data.remote.dto.first_aid.UpsertFirstAidRequest
import org.rol.transportation.utils.Constants

class FirstAidApi(private val client: HttpClient) {

    suspend fun getFirstAid(
        vehiculoId: Int,
        documentId: Int? = null
    ): FirstAidDto? {
        val response = client.get("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/inspeccion-botiquines/find") {
            documentId?.let { parameter("documentId", it) }
            expectSuccess = false
        }
        return when (response.status.value) {
            200 -> response.body<FirstAidDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}")
        }
    }

    suspend fun upsertFirstAid(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: UpsertFirstAidRequest
    ): FirstAidDto {
        return client.post("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/inspeccion-botiquines/upsert") {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}