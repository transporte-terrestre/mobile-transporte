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

            // Primero obtenemos el checklist actual para tener la información completa
            val currentChecklist = checklistApi.getTripChecklist(tripId, tipo.value)

            // Creamos el request con la información actualizada
            val request = UpsertChecklistRequest(
                items = currentChecklist.items.map { item ->
                    // Buscamos si este item fue marcado/desmarcado
                    val wasUpdated = items.find { it.first == item.checklistItemId }

                    UpsertChecklistItemRequest(
                        checklistItemId = item.checklistItemId,
                        vehiculoChecklistDocumentId = if (wasUpdated?.second == true) {
                            // Si fue marcado como completado, mantenemos o asignamos un ID
                            item.vehiculoChecklistDocumentId ?: item.checklistItemId
                        } else {
                            // Si no está completado, enviamos null
                            null
                        },
                        observacion = if (item.checklistItemId == wasUpdated?.first) {
                            observaciones
                        } else {
                            item.observacion
                        }
                    )
                }
            )

            val checklist = checklistApi.upsertTripChecklist(tripId, tipo.value, request)
            emit(Resource.Success(checklist.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar el checklist"))
        }
    }


    override suspend fun verifyTripChecklist(
        tripId: Int,
        tipo: ChecklistType
    ): Flow<Resource<TripChecklist>> = flow {
        try {
            emit(Resource.Loading)
            val checklist = checklistApi.verifyTripChecklist(tripId, tipo.value)
            emit(Resource.Success(checklist.toDomain()))
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = e.message ?: ""
            if (msg.contains("JsonConvertException") || msg.contains("required for type")) {
                emit(Resource.Error("El servidor rechazó la validación (Error 500 interno). Intente nuevamente más tarde."))
            } else {
                emit(Resource.Error(e.message ?: "Error desconocido"))
            }
        }
    }


    private fun ChecklistItemDto.toDomain() = ChecklistItem(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        orden = orden,
        creadoEn = creadoEn,
        actualizadoEn = actualizadoEn,
        eliminadoEn = eliminadoEn
    )

    private fun TripChecklistDto.toDomain() = TripChecklist(
        id = id,
        viajeId = viajeId,
        tipo = ChecklistType.fromString(tipo),
        items = items.map { item ->
            ChecklistItemDetail(
                checklistItemId = item.checklistItemId,
                vehiculoChecklistDocumentId = item.vehiculoChecklistDocumentId,
                nombre = item.nombre,
                descripcion = item.descripcion,
                orden = item.orden,
                observacion = item.observacion,
                creadoEn = item.creadoEn,
                actualizadoEn = item.actualizadoEn,
                isUpdate = item.isUpdate
            )
        },
        creadoEn = creadoEn,
        actualizadoEn = actualizadoEn
    )
}