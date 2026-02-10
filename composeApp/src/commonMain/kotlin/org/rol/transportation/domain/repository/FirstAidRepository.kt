package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.first_aid.FirstAidInspection
import org.rol.transportation.utils.Resource

interface FirstAidRepository {
    suspend fun getFirstAid(vehiculoId: Int, documentId: Int?): Flow<Resource<FirstAidInspection>>
    suspend fun upsertFirstAid(vehiculoId: Int, viajeId: Int, viajeTipo: TripType, inspection: FirstAidInspection): Flow<Resource<FirstAidInspection>>
}