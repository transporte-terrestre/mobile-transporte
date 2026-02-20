package org.rol.transportation.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.paging.TripPagingSource
import org.rol.transportation.data.remote.api.TripApi
import org.rol.transportation.data.remote.dto.trip.TripDto
import org.rol.transportation.domain.model.Customer
import org.rol.transportation.domain.model.Driver
import org.rol.transportation.domain.model.Route
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.model.Vehicle
import org.rol.transportation.domain.model.enums.TripStatus
import org.rol.transportation.utils.Resource

class TripRepositoryImpl(
    private val tripApi: TripApi
) : TripRepository {

    override fun getTripsPaged(): Flow<PagingData<Trip>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                TripPagingSource(
                    tripApi = tripApi,
                    toDomain = { it.toDomain() }
                )
            }
        ).flow
    }

    override fun getLightTripsPaged(): Flow<PagingData<org.rol.transportation.domain.model.TripLight>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                org.rol.transportation.data.paging.TripLightPagingSource(
                    tripApi = tripApi,
                    toDomain = { it.toDomain() }
                )
            }
        ).flow
    }

    override suspend fun getTripById(id: Int): Flow<Resource<Trip>> = flow {
        try {
            emit(Resource.Loading)

            val tripDto = tripApi.getTripById(id)
            val trip = tripDto.toFlatTrip()

            emit(Resource.Success(trip))
        } catch (e: Exception) {
            val msg = e.message ?: ""
            if (msg.contains("Expected response body") || msg.contains("NoTransformationFoundException")) {
                emit(Resource.Error("El viaje solicitado no devolvió datos (posiblemente eliminado o corrupto en el servidor)."))
            } else {
                emit(Resource.Error(e.message ?: "Error al obtener el viaje"))
            }
        }
    }

    private fun org.rol.transportation.data.remote.dto.trip.TripSegmentDto.toDomain(): org.rol.transportation.domain.model.TripSegment {
        return org.rol.transportation.domain.model.TripSegment(
            id = id,
            cliente = cliente?.let {
                Customer(
                    id = it.id,
                    nombreCompleto = it.nombreCompleto,
                    ruc = it.ruc,
                    razonSocial = it.razonSocial,
                    email = it.email,
                    telefono = it.telefono,
                    direccion = it.direccion,
                    imagenes = it.imagenes
                )
            },
            ruta = ruta?.let {
                Route(
                    id = it.id,
                    origen = it.origen ?: "",
                    destino = it.destino ?: "",
                    origenLat = it.origenLat?.toDoubleOrNull() ?: 0.0,
                    origenLng = it.origenLng?.toDoubleOrNull() ?: 0.0,
                    destinoLat = it.destinoLat?.toDoubleOrNull() ?: 0.0,
                    destinoLng = it.destinoLng?.toDoubleOrNull() ?: 0.0,
                    distancia = it.distancia ?: "0.0"
                )
            },
            rutaOcasional = rutaOcasional,
            conductorPrincipal = (conductorPrincipal ?: conductores?.firstOrNull { it.esPrincipal })?.let {
                Driver(
                    id = it.id,
                    nombreCompleto = it.nombreCompleto,
                    numeroLicencia = it.numeroLicencia ?: "S/N",
                    rol = it.rol ?: "Conductor",
                    esPrincipal = true,
                    fotocheck = it.fotocheck
                )
            },
            vehiculoPrincipal = (vehiculoPrincipal ?: vehiculos?.firstOrNull { it.esPrincipal })?.let {
                Vehicle(
                    id = it.id,
                    marca = it.marca ?: "",
                    modelo = it.modelo ?: "",
                    placa = it.placa ?: "",
                    anio = it.anio ?: 2024,
                    esPrincipal = true,
                    imagenes = it.imagenes
                )
            },
            estado = TripStatus.fromString(estado ?: "programado"), // Fallback if missing
            fechaSalida = fechaSalida ?: "",
            fechaLlegada = fechaLlegada,
            distanciaEstimada = distanciaEstimada,
            distanciaFinal = distanciaFinal,
            modalidadServicio = modalidadServicio,
            turno = turno,
            sentido = sentido,
            checkInSalida = checkInSalida ?: false,
            checkInLlegada = checkInLlegada ?: false
        )
    }

    private fun org.rol.transportation.data.remote.dto.trip.TripSegmentDto.toFlatTrip(): Trip {
        return Trip(
            id = id,
            creadoEn = fechaSalida ?: "", // Using fechaSalida as a fallback for creadoEn since the flat DTO may not have it uniformly named in the segment block if we rely on it.
            ida = this.toDomain(),
            vuelta = null
        )
    }

    private fun TripDto.toDomain(): Trip {
        return Trip(
            id = id,
            creadoEn = creadoEn,
            ida = ida.toDomain(),
            vuelta = vuelta?.toDomain()
        )
    }

    private fun org.rol.transportation.data.remote.dto.trip.TripLightDto.toDomain(): org.rol.transportation.domain.model.TripLight {
        return org.rol.transportation.domain.model.TripLight(
            id = id,
            ida = ida.toDomain(),
            vuelta = vuelta?.toDomain()
        )
    }

    private fun org.rol.transportation.data.remote.dto.trip.TripLightSegmentDto.toDomain(): org.rol.transportation.domain.model.TripLightSegment {
        return org.rol.transportation.domain.model.TripLightSegment(
            id = id,
            rutaNombre = rutaNombre ?: "Sin Ruta",
            estado = estado ?: "programado",
            fecha = fecha ?: "",
            checkInSalida = checkInSalida,
            checkInLlegada = checkInLlegada
        )
    }
}