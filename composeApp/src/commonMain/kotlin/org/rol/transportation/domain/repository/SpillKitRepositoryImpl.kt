package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.rol.transportation.data.remote.api.SpillKitApi
import org.rol.transportation.data.remote.dto.spill_kit.SpillKitDto
import org.rol.transportation.data.remote.dto.spill_kit.SpillKitItemDto
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.spill_kit.SpillKitInspection
import org.rol.transportation.domain.model.spill_kit.SpillKitItem
import org.rol.transportation.utils.Resource


class SpillKitRepositoryImpl(private val api: SpillKitApi) : SpillKitRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getSpillKit(vehiculoId: Int, documentId: Int?): Flow<Resource<SpillKitInspection>> = flow {
        try {
            emit(Resource.Loading)
            val dto = api.getSpillKit(vehiculoId, documentId)

            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("El servidor no devolvió la estructura del kit antiderrames."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener kit antiderrames"))
        }
    }

    override suspend fun upsertSpillKit(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: SpillKitInspection
    ): Flow<Resource<SpillKitInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertSpillKit(vehiculoId, viajeId, viajeTipo.value, request)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar kit"))
        }
    }

    // --- MAPPERS ---
    private fun SpillKitDto.toDomain(): SpillKitInspection {
        var location = ""
        val itemsMap = mutableMapOf<String, SpillKitItem>()

        // Recorremos el JsonObject dinámicamente
        document.forEach { (key, element) ->
            if (key == "ubicacion") {
                location = element.jsonPrimitive.content
            } else {
                // Es un item (objeto)
                try {
                    val itemDto = json.decodeFromJsonElement<SpillKitItemDto>(element)
                    itemsMap[key] = SpillKitItem(itemDto.label, itemDto.estado)
                } catch (e: Exception) {
                    // Ignorar claves extrañas
                }
            }
        }

        return SpillKitInspection(
            viajeId = viajeId, vehiculoId = vehiculoId, version = version, viajeTipo = viajeTipo,
            location = location,
            items = itemsMap
        )
    }

    private fun SpillKitInspection.toUpsertRequest(): JsonObject {
        return buildJsonObject {
            // Agregamos los items (Solo el booleano, según tu curl)
            items.forEach { (key, item) ->
                put(key, item.estado) // "mazoGoma": true
            }
            put("ubicacion", location)
        }
    }

}