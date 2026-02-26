package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.ProximoTramoDto
import org.rol.transportation.data.remote.dto.trip_services.RegistrarServicioRequest
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto

class TripServicesApi(private val client: HttpClient) {

    suspend fun getTripSegments(tripId: Int): List<SegmentDto> {
        return client.get("viaje/$tripId/servicios").body()
    }

    suspend fun getNextStep(tripId: Int): NextStepDto {
        return client.get("viaje/$tripId/servicio/next-step").body()
    }

    suspend fun getProximoTramo(tripId: Int): ProximoTramoDto {
        return client.get("viaje/$tripId/proximo-tramo").body()
    }

    // ── Endpoints de registro por tipo ──

    suspend fun registrarSalida(tripId: Int, request: RegistrarServicioRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-salida") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registrarLlegada(tripId: Int, request: RegistrarServicioRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-llegada") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registrarPunto(tripId: Int, request: RegistrarServicioRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-punto") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registrarParada(tripId: Int, request: RegistrarServicioRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-parada") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun registrarDescanso(tripId: Int, request: RegistrarServicioRequest): SegmentDto {
        return client.post("viaje/$tripId/registrar-descanso") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    /**
     * Enruta al endpoint correcto según el tipo del próximo tramo.
     */
    suspend fun registrarServicio(tripId: Int, tipo: String, request: RegistrarServicioRequest): SegmentDto {
        return when (tipo.lowercase()) {
            "origen" -> registrarSalida(tripId, request)
            "destino" -> registrarLlegada(tripId, request)
            "punto" -> registrarPunto(tripId, request)
            "parada" -> registrarParada(tripId, request)
            "descanso" -> registrarDescanso(tripId, request)
            else -> throw IllegalArgumentException("Tipo de servicio desconocido: $tipo")
        }
    }
}