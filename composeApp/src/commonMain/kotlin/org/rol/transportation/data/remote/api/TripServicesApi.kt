package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.patch
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.passenger.PassengerAttendanceDto
import org.rol.transportation.data.remote.dto.passenger.PassengerUpsertRequest
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest
import org.rol.transportation.utils.Constants

class TripServicesApi (private val client: HttpClient) {

    suspend fun getTripSegments(tripId: Int): List<SegmentDto> {
        return client.get("viaje/$tripId/servicios").body()
    }

    // Parameter `tipo` is optional in the getNextStep API
    suspend fun getNextStep(tripId: Int, tipo: String? = null): NextStepDto {
        return client.get("viaje/$tripId/proximo-tramo") {
            tipo?.let { parameter("tipo", it) }
        }.body()
    }

    suspend fun registerDeparture(tripId: Int, request: RegisterLocationRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-salida") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registerArrival(tripId: Int, request: RegisterLocationRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-llegada") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registerCheckpoint(tripId: Int, request: RegisterLocationRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-punto") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registerStop(tripId: Int, request: RegisterLocationRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-parada") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registerRest(tripId: Int, request: RegisterLocationRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-descanso") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateSegment(segmentId: Int, request: UpdateSegmentRequest): SegmentDto {
        return client.patch("viaje/servicio/update/$segmentId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteSegment(segmentId: Int): SegmentDto {
        return client.delete("viaje/servicio/delete/$segmentId").body()
    }

}