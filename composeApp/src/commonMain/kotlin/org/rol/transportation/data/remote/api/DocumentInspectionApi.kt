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
import org.rol.transportation.data.remote.dto.document_inspection.DocumentInspectionDto
import org.rol.transportation.data.remote.dto.document_inspection.UpsertDocumentInspectionRequest
import org.rol.transportation.utils.Constants

class DocumentInspectionApi(private val client: HttpClient) {

    suspend fun getDocumentInspection(
        vehiculoId: Int,
        documentId: Int? = null
    ): DocumentInspectionDto? {
        val response = client.get(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/inspeccion-documentos/find"
        ) {
            documentId?.let {
                parameter("documentId", it)
            }
            expectSuccess = false
        }

        return when (response.status.value) {
            200 -> response.body<DocumentInspectionDto>()
            404 -> null
            else -> throw Exception("Error HTTP ${response.status.value}")
        }
    }

    suspend fun upsertDocumentInspection(
        vehiculoId: Int,
        viajeId: Int,
        tipo: String,
        request: UpsertDocumentInspectionRequest
    ): DocumentInspectionDto {
        return client.post(
            "${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/inspeccion-documentos/upsert"
        ) {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}