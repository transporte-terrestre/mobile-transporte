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
import org.rol.transportation.data.remote.dto.tools_inspection.ToolsInspectionDto
import org.rol.transportation.data.remote.dto.tools_inspection.UpsertToolsRequest
import org.rol.transportation.utils.Constants

class ToolsInspectionApi(private val client: HttpClient) {

    suspend fun getTools(
        vehiculoId: Int,
        documentId: Int? = null
    ): ToolsInspectionDto? {
        val response = client.get("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/inspeccion-herramientas/find") {
            documentId?.let { parameter("documentId", it) }
            expectSuccess = false
        }
        return when (response.status.value) {
            200 -> response.body<ToolsInspectionDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}")
        }
    }

    suspend fun upsertTools(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: UpsertToolsRequest
    ): ToolsInspectionDto {
        return client.post("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/inspeccion-herramientas/upsert") {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}