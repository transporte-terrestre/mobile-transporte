package org.rol.transportation.domain.repository

import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.TripServicesApi
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.ProximoTramoDto
import org.rol.transportation.data.remote.dto.trip_services.RegistrarServicioRequest
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.utils.Resource

class TripServicesRepositoryImpl(
    private val api: TripServicesApi
) : TripServicesRepository {

    override suspend fun getTripSegments(tripId: Int): Flow<Resource<List<SegmentDto>>> = flow {
        try {
            emit(Resource.Loading)
            val segments = api.getTripSegments(tripId)
            emit(Resource.Success(segments))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al cargar tramos"))
        }
    }

    override suspend fun getNextStep(tripId: Int): Flow<Resource<NextStepDto>> = flow {
        try {
            val nextStep = api.getNextStep(tripId)
            emit(Resource.Success(nextStep))
        } catch (e: ClientRequestException) {
            if (e.response.status.value == 409) {
                emit(Resource.Error("Ruta no válida"))
            } else {
                emit(Resource.Error("Error: ${e.response.status.value}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    override suspend fun getProximoTramo(tripId: Int): Flow<Resource<ProximoTramoDto>> = flow {
        try {
            emit(Resource.Loading)
            val data = api.getProximoTramo(tripId)
            emit(Resource.Success(data))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener próximo tramo"))
        }
    }

    override suspend fun registrarServicio(
        tripId: Int,
        tipo: String,
        request: RegistrarServicioRequest
    ): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registrarServicio(tripId, tipo, request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar servicio"))
        }
    }
}