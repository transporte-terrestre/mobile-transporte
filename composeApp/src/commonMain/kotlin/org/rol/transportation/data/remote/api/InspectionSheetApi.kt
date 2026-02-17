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
import org.rol.transportation.data.remote.dto.inspection_sheet.InspectionSheetDto
import org.rol.transportation.data.remote.dto.inspection_sheet.UpsertInspectionSheetRequest
import org.rol.transportation.utils.Constants

class InspectionSheetApi(private val client: HttpClient) {

    suspend fun getInspectionSheet(
        vehiculoId: Int,
        documentId: Int? = null
    ): InspectionSheetDto? {
        val response = client.get(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/hoja-inspeccion/find"
        ) {
            documentId?.let { parameter("documentId", it) }
            expectSuccess = false
        }

        return when (response.status.value) {
            200 -> response.body<InspectionSheetDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}: ${response.status.description}")
        }
    }

    suspend fun upsertInspectionSheet(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: UpsertInspectionSheetRequest
    ): InspectionSheetDto {
        return client.post(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/hoja-inspeccion/upsert"
        ) {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}