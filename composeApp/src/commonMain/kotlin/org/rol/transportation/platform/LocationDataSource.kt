package org.rol.transportation.platform

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.LocationModel

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class LocationDataSource() {
    fun getLocationUpdates(): Flow<LocationModel>
    suspend fun getLastLocation(): LocationModel?
    fun stopUpdates()
    fun isLocationEnabled(): Boolean
    fun openLocationSettings()
}
