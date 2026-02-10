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
import org.rol.transportation.data.remote.dto.trip_services.CreateSegmentRequest
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.utils.Constants

class TripServicesApi (private val client: HttpClient) {

    suspend fun getTripSegments(tripId: Int): List<SegmentDto> {
        return client.get("viaje/$tripId/servicios").body()
    }

    suspend fun getNextStep(tripId: Int): NextStepDto {
        return client.get("viaje/$tripId/servicio/next-step").body()
    }

    suspend fun createSegment(tripId: Int, request: CreateSegmentRequest): SegmentDto {
        return client.post("viaje/$tripId/servicio/create") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}