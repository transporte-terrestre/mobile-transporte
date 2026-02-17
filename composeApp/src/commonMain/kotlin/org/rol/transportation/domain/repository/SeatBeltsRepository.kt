package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltsInspection
import org.rol.transportation.utils.Resource

interface SeatBeltsRepository {
    suspend fun getSeatBelts(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<SeatBeltsInspection>>

    suspend fun upsertSeatBelts(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: SeatBeltsInspection
    ): Flow<Resource<SeatBeltsInspection>>
}