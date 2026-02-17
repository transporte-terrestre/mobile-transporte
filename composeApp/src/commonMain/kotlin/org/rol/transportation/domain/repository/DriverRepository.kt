package org.rol.transportation.domain.repository

import org.rol.transportation.domain.model.DriverDetail

interface DriverRepository {
    suspend fun getDriverById(id: Int): DriverDetail
}
