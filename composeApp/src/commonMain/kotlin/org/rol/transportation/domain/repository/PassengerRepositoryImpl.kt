package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.PassengerApi
import org.rol.transportation.data.remote.dto.passenger.ScanDnisRequest
import org.rol.transportation.data.remote.dto.passenger.ScanDnisResponse
import org.rol.transportation.domain.model.Passenger
import org.rol.transportation.utils.Resource

class PassengerRepositoryImpl (private val api: PassengerApi) : PassengerRepository {
    override suspend fun getPassengers(tripId: Int, viajeTramoId: Int?): Flow<Resource<List<Passenger>>> = flow {
        try {
            emit(Resource.Loading)
            val dtos = api.getPassengers(tripId, viajeTramoId)
            val domainList = dtos.map { dto ->
                val p = dto.pasajero
                val dniStr = p?.dni ?: dto.dni ?: ""
                val nombres = p?.nombres ?: dto.nombres ?: ""
                val apellidos = p?.apellidos ?: dto.apellidos ?: ""
                
                Passenger(
                    id = dto.id ?: 0,
                    viajeId = tripId,
                    pasajeroId = dto.pasajeroId,
                    dni = dniStr,
                    nombreCompleto = "$nombres $apellidos".trim(),
                    asistencia = dto.asistencia
                )
            }
            emit(Resource.Success(domainList))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al cargar pasajeros"))
        }
    }

    override suspend fun scanDnis(
        tripId: Int,
        viajeTramoId: Int,
        urls: List<String>
    ): Flow<Resource<ScanDnisResponse>> = flow {
        try {
            emit(Resource.Loading)
            val request = ScanDnisRequest(urls = urls)
            val response = api.scanDnis(tripId, viajeTramoId, request)
            if (response.exito) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.mensaje))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al escanear DNIs"))
        }
    }
}