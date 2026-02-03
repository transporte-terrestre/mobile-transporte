package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.ChecklistApi
import org.rol.transportation.data.remote.dto.check.ChecklistItemDto
import org.rol.transportation.data.remote.dto.check.TripChecklistDto
import org.rol.transportation.data.remote.dto.check.UpsertChecklistItemRequest
import org.rol.transportation.data.remote.dto.check.UpsertChecklistRequest
import org.rol.transportation.domain.model.ChecklistItem
import org.rol.transportation.domain.model.ChecklistItemDetail
import org.rol.transportation.domain.model.TripChecklist
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.utils.Resource

class ChecklistRepositoryImpl(
    private val checklistApi: ChecklistApi
) : ChecklistRepository {

    override suspend fun getAllChecklistItems(): Flow<Resource<List<ChecklistItem>>> = flow {
        try {
            emit(Resource.Loading)
            val items = checklistApi.getAllChecklistItems()
            val checklistItems = items.map { it.toDomain() }
            emit(Resource.Success(checklistItems))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener items del checklist"))
        }
    }

    override suspend fun getTripChecklist(
        tripId: Int,
        tipo: ChecklistType
    ): Flow<Resource<TripChecklist>> = flow {
        try {
            emit(Resource.Loading)
            val checklist = checklistApi.getTripChecklist(tripId, tipo.value)
            emit(Resource.Success(checklist.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener el checklist"))
        }
    }


    override suspend fun upsertTripChecklist(
        tripId: Int,
        tipo: ChecklistType,
        items: List<Pair<Int, Boolean>>,
        observaciones: String?
    ): Flow<Resource<TripChecklist>> = flow {
        try {
            emit(Resource.Loading)

            val request = UpsertChecklistRequest(
                observaciones = observaciones,
                items = items.map { (itemId, completado) ->
                    UpsertChecklistItemRequest(
                        id = itemId, // checklistItemId
                        completado = completado
                    )
                }
            )

            val checklist = checklistApi.upsertTripChecklist(tripId, tipo.value, request)
            emit(Resource.Success(checklist.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar el checklist"))
        }
    }

    private fun ChecklistItemDto.toDomain() = ChecklistItem(
        id = id,
        seccion = seccion,
        nombre = nombre,
        descripcion = descripcion,
        icono = icono,
        orden = orden
    )

    private fun TripChecklistDto.toDomain() = TripChecklist(
        id = id,
        viajeId = viajeId,
        tipo = ChecklistType.fromString(tipo),
        validadoEn = validadoEn,
        observaciones = observaciones,
        items = items.map { item ->
            ChecklistItemDetail(
                checklistItemId = item.checklistItemId,
                nombre = item.nombre,
                descripcion = item.descripcion,
                completado = item.completado,
                seccion = item.seccion,
                orden = item.orden
            )
        },
        message = message
    )
}