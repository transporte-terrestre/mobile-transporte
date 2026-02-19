package org.rol.transportation.domain.repository

import org.rol.transportation.domain.model.driver_documents.DriverDetail

interface DriverRepository {
    suspend fun getDriverWithDocuments(id: Int): DriverDetail
}
