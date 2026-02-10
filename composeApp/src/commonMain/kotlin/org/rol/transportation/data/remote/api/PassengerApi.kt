package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.passenger.PassengerAttendanceDto
import org.rol.transportation.data.remote.dto.passenger.PassengerUpsertRequest
import org.rol.transportation.utils.Constants

class PassengerApi(private val client: HttpClient) {

    suspend fun getPassengers(tripId: Int): List<PassengerAttendanceDto> {
        return client.get("${Constants.TRIP}/$tripId/pasajeros").body()
    }

    suspend fun upsertPassengers(tripId: Int, request: PassengerUpsertRequest): List<PassengerAttendanceDto> {
        return client.post("${Constants.TRIP}/$tripId/pasajeros/upsert") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}