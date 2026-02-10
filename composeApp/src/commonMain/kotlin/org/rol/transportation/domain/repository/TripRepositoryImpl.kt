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

    override suspend fun getTripById(id: Int): Flow<Resource<Trip>> = flow {
        try {
            emit(Resource.Loading)

            val tripDto = tripApi.getTripById(id)
            val trip = tripDto.toDomain()

            emit(Resource.Success(trip))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener el viaje"))
        }
    }

    private fun TripDto.toDomain(): Trip {
        return Trip(
            id = id,
            cliente = Customer(
                id = cliente.id,
                nombreCompleto = cliente.nombreCompleto,
                email = cliente.email,
                telefono = cliente.telefono,
                direccion = cliente.direccion,
                imagenes = cliente.imagenes
            ),
            ruta = ruta?.let {
                Route(
                    id = it.id,
                    origen = it.origen,
                    destino = it.destino,
                    origenLat = it.origenLat.toDoubleOrNull() ?: 0.0,
                    origenLng = it.origenLng.toDoubleOrNull() ?: 0.0,
                    destinoLat = it.destinoLat.toDoubleOrNull() ?: 0.0,
                    destinoLng = it.destinoLng.toDoubleOrNull() ?: 0.0,
                    distancia = it.distancia
                )
            },
            rutaOcasional = rutaOcasional,
            conductores = conductores.map {
                Driver(
                    id = it.id,
                    nombreCompleto = it.nombreCompleto,
                    numeroLicencia = it.numeroLicencia,
                    rol = it.rol,
                    esPrincipal = it.esPrincipal,
                    fotocheck = it.fotocheck
                )
            },
            vehiculos = vehiculos.map {
                Vehicle(
                    id = it.id,
                    marca = it.marca,
                    modelo = it.modelo,
                    placa = it.placa,
                    anio = it.anio,
                    esPrincipal = it.esPrincipal,
                    imagenes = it.imagenes
                )
            },
            estado = TripStatus.fromString(estado),
            fechaSalida = fechaSalida,
            fechaLlegada = fechaLlegada,
            distanciaEstimada = distanciaEstimada,
            distanciaFinal = distanciaFinal,
            modalidadServicio = modalidadServicio,
            tripulantes = tripulantes
        )
    }
}