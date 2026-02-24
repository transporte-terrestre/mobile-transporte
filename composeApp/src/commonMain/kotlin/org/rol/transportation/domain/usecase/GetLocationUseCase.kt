package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.LocationModel
import org.rol.transportation.domain.repository.LocationRepository

class GetLocationUseCase(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<LocationModel> = repository.getLocationUpdates()

    suspend fun getLastKnown(): LocationModel? = repository.getLastLocation()

    fun isLocationEnabled(): Boolean = repository.isLocationEnabled()

    fun openLocationSettings() = repository.openLocationSettings()
}
