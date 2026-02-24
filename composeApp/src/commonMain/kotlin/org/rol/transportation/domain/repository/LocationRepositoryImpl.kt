package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.LocationModel
import org.rol.transportation.platform.LocationDataSource

class LocationRepositoryImpl(
    private val dataSource: LocationDataSource
) : LocationRepository {

    override fun getLocationUpdates(): Flow<LocationModel> =
        dataSource.getLocationUpdates()

    override suspend fun getLastLocation(): LocationModel? =
        dataSource.getLastLocation()

    override fun isLocationEnabled(): Boolean =
        dataSource.isLocationEnabled()

    override fun openLocationSettings() =
        dataSource.openLocationSettings()
}
