package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.check.ChecklistItemDto
import org.rol.transportation.data.remote.dto.check.TripChecklistDto
import org.rol.transportation.data.remote.dto.check.UpsertChecklistRequest
import org.rol.transportation.utils.Constants

class ChecklistApi(private val client: HttpClient) {

    suspend fun getAllChecklistItems(): List<ChecklistItemDto> {
        return client.get(Constants.CHECKLIST_ITEMS_ENDPOINT).body()
    }

    suspend fun getTripChecklist(tripId: Int, tipo: String): TripChecklistDto {
        return client.get("${Constants.TRIP_CHECKLIST_ENDPOINT}/$tripId/checklist") {
            parameter("tipo", tipo)
        }.body()
    }


    suspend fun upsertTripChecklist(
        tripId: Int,
        tipo: String,
        request: UpsertChecklistRequest
    ): TripChecklistDto {
        return client.post("${Constants.TRIP_CHECKLIST_ENDPOINT}/$tripId/checklist/upsert") {
            parameter("tipo", tipo)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}