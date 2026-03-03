package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.passenger.PassengerAttendanceDto
import org.rol.transportation.data.remote.dto.passenger.ScanDnisRequest
import org.rol.transportation.data.remote.dto.passenger.ScanDnisResponse
import org.rol.transportation.utils.Constants

class PassengerApi(private val client: HttpClient) {

    suspend fun getPassengers(tripId: Int, viajeTramoId: Int? = null): List<PassengerAttendanceDto> {
        val query = if (viajeTramoId != null) "?viajeTramoId=$viajeTramoId" else ""
        return client.get("${Constants.TRIP}/$tripId/pasajeros$query").body()
    }

    suspend fun scanDnis(tripId: Int, viajeTramoId: Int, request: ScanDnisRequest): ScanDnisResponse {
        return client.post("${Constants.TRIP}/$tripId/pasajeros/escanear-dnis?viajeTramoId=$viajeTramoId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}