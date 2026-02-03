package org.rol.transportation.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.utils.Resource

interface TripRepository {
    fun getTripsPaged(): Flow<PagingData<Trip>>
    suspend fun getTripById(id: Int): Flow<Resource<Trip>>
}