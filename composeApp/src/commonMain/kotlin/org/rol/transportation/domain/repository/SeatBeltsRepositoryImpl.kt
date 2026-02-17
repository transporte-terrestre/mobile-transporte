package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.SeatBeltsApi
import org.rol.transportation.data.remote.dto.seat_belts.SeatBeltItemDto
import org.rol.transportation.data.remote.dto.seat_belts.SeatBeltsDto
import org.rol.transportation.data.remote.dto.seat_belts.UpsertSeatBeltItemDto
import org.rol.transportation.data.remote.dto.seat_belts.UpsertSeatBeltsRequest
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltItem
import org.rol.transportation.domain.model.seat_belts.SeatBeltsInspection
import org.rol.transportation.utils.Resource


class SeatBeltsRepositoryImpl(private val api: SeatBeltsApi) : SeatBeltsRepository {

    override suspend fun getSeatBelts(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<SeatBeltsInspection>> = flow {
        try {
            emit(Resource.Loading)

            val dto = api.getSeatBelts(vehiculoId, documentId)

            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("El servidor no devolvió la estructura de cinturones."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener cinturones"))
        }
    }

    override suspend fun upsertSeatBelts(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: SeatBeltsInspection
    ): Flow<Resource<SeatBeltsInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertSeatBelts(vehiculoId, viajeId, viajeTipo.value, request)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar cinturones"))
        }
    }

    private fun SeatBeltsDto.toDomain() = SeatBeltsInspection(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        items = document.mapValues { it.value.toDomain() }
    )

    private fun SeatBeltItemDto.toDomain() = SeatBeltItem(
        label = label,
        habilitado = habilitado,
        observacion = observacion ?: ""
    )

    private fun SeatBeltsInspection.toUpsertRequest(): UpsertSeatBeltsRequest {
        return items.mapValues { (_, item) ->
            UpsertSeatBeltItemDto(
                habilitado = item.habilitado,
                observacion = if (item.observacion.isBlank()) null else item.observacion
            )
        }
    }

}