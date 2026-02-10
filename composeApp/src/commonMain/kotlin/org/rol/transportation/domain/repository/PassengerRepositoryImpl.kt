package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.LightsAlarmApi
import org.rol.transportation.data.remote.api.PassengerApi
import org.rol.transportation.data.remote.dto.passenger.PassengerAttendanceDto
import org.rol.transportation.data.remote.dto.passenger.PassengerUpsertRequest
import org.rol.transportation.domain.model.Passenger
import org.rol.transportation.utils.Resource

class PassengerRepositoryImpl (private val api: PassengerApi) : PassengerRepository {
    override suspend fun getPassengers(tripId: Int): Flow<Resource<List<Passenger>>> = flow {
        try {
            emit(Resource.Loading)
            val dtos = api.getPassengers(tripId)
            val domainList = dtos.map { dto ->
                Passenger(
                    viajeId = tripId,
                    pasajeroId = dto.pasajeroId,
                    dni = dto.pasajero?.dni ?: "",
                    nombreCompleto = "${dto.pasajero?.nombres} ${dto.pasajero?.apellidos}",
                    asistencia = dto.asistencia
                )
            }
            emit(Resource.Success(domainList))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al cargar pasajeros"))
        }
    }

    override suspend fun upsertPassengers(
        tripId: Int,
        passengers: List<Passenger>
    ): Flow<Resource<List<Passenger>>> = flow {
        try {
            emit(Resource.Loading)
            val requestDto = PassengerUpsertRequest(
                pasajeros = passengers.map { p ->
                    PassengerAttendanceDto(
                        pasajeroId = p.pasajeroId,
                        asistencia = p.asistencia
                    )
                }
            )
            val responseDtos = api.upsertPassengers(tripId, requestDto)

            // Mapeamos la respuesta de vuelta a dominio
            val domainList = responseDtos.map { dto ->
                Passenger(
                    viajeId = tripId,
                    pasajeroId = dto.pasajeroId,
                    dni = dto.pasajero?.dni ?: "",
                    nombreCompleto = "${dto.pasajero?.nombres} ${dto.pasajero?.apellidos}",
                    asistencia = dto.asistencia
                )
            }
            emit(Resource.Success(domainList))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar asistencia"))
        }
    }
}