package org.rol.transportation.domain.repository

import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.TripServicesApi
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest
import org.rol.transportation.utils.Resource

class TripServicesRepositoryImpl(
    private val api: TripServicesApi
): TripServicesRepository {
    override suspend fun getTripSegments(tripId: Int): Flow<Resource<List<SegmentDto>>> = flow {
        try {
            emit(Resource.Loading)
            val segments = api.getTripSegments(tripId)
            emit(Resource.Success(segments))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al cargar tramos"))
        }
    }

    override suspend fun getNextStep(tripId: Int, tipo: String?): Flow<Resource<NextStepDto>> = flow {
        try {
            val nextStep = api.getNextStep(tripId, tipo)
            emit(Resource.Success(nextStep))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido al obtener próximo tramo"))
        }
    }

    override suspend fun registerDeparture(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registerDeparture(tripId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar salida"))
        }
    }

    override suspend fun registerArrival(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registerArrival(tripId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar llegada"))
        }
    }

    override suspend fun registerCheckpoint(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registerCheckpoint(tripId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar punto"))
        }
    }

    override suspend fun registerStop(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registerStop(tripId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar parada ocasional"))
        }
    }

    override suspend fun registerRest(tripId: Int, request: RegisterLocationRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.registerRest(tripId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al registrar descanso"))
        }
    }

    override suspend fun updateSegment(segmentId: Int, request: UpdateSegmentRequest): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.updateSegment(segmentId, request)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al actualizar tramo"))
        }
    }

    override suspend fun deleteSegment(segmentId: Int): Flow<Resource<SegmentDto>> = flow {
        try {
            emit(Resource.Loading)
            val response = api.deleteSegment(segmentId)
            emit(Resource.Success(response))
        } catch (e: ClientRequestException) {
            emit(Resource.Error("Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al eliminar tramo"))
        }
    }
}