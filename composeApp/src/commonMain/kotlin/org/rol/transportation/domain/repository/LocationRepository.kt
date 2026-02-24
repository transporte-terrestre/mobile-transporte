package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.LocationModel

interface LocationRepository {
    fun getLocationUpdates(): Flow<LocationModel>
    suspend fun getLastLocation(): LocationModel?
    fun isLocationEnabled(): Boolean
    fun openLocationSettings()
}
