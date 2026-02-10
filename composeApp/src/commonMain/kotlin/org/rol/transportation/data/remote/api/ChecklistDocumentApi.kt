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
import org.rol.transportation.data.remote.dto.check_document.ChecklistDocumentDto
import org.rol.transportation.data.remote.dto.check_document.UpsertChecklistDocumentRequest
import org.rol.transportation.utils.Constants

class ChecklistDocumentApi(private val client: HttpClient) {

    suspend fun getChecklistDocument(
        vehiculoId: Int,
        documentType: String,
        documentId: Int? = null
    ): ChecklistDocumentDto? {
        val response = client.get("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/checklist-document/$documentType/find") {
            documentId?.let {
                parameter("documentId", it)
            }
            expectSuccess = false
        }

        return when (response.status.value) {
            200 -> response.body<ChecklistDocumentDto>()
            404, 409 -> null
            else -> throw Exception("Error HTTP ${response.status.value}: ${response.status.description}")
        }
    }

    suspend fun upsertChecklistDocument(
        vehiculoId: Int,
        viajeId: Int,
        documentType: String,
        tipo: String,
        request: UpsertChecklistDocumentRequest
    ): ChecklistDocumentDto {
        return client.post("${Constants.VEHICULO_ENDPOINT}/$vehiculoId/viaje/$viajeId/checklist-document/$documentType/upsert") {
            contentType(ContentType.Application.Json)
            parameter("tipo", tipo)
            setBody(request)
        }.body()
    }
}