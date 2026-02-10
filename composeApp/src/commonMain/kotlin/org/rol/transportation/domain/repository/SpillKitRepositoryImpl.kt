package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.rol.transportation.data.remote.api.SeatBeltsApi
import org.rol.transportation.data.remote.api.SpillKitApi
import org.rol.transportation.data.remote.dto.seat_belts.SeatBeltItemDto
import org.rol.transportation.data.remote.dto.seat_belts.SeatBeltsDto
import org.rol.transportation.data.remote.dto.seat_belts.UpsertSeatBeltItemDto
import org.rol.transportation.data.remote.dto.seat_belts.UpsertSeatBeltsRequest
import org.rol.transportation.data.remote.dto.spill_kit.SpillKitDto
import org.rol.transportation.data.remote.dto.spill_kit.SpillKitItemDto
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltItem
import org.rol.transportation.domain.model.seat_belts.SeatBeltsInspection
import org.rol.transportation.domain.model.spill_kit.SpillKitInspection
import org.rol.transportation.domain.model.spill_kit.SpillKitItem
import org.rol.transportation.utils.Resource


class SpillKitRepositoryImpl(private val api: SpillKitApi) : SpillKitRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getSpillKit(vehiculoId: Int, documentId: Int?): Flow<Resource<SpillKitInspection>> = flow {
        try {
            emit(Resource.Loading)
            // LÓGICA DE AISLAMIENTO: ID null -> No llamar API
            if (documentId != null) {
                val dto = api.getSpillKit(vehiculoId, documentId)
                if (dto != null) emit(Resource.Success(dto.toDomain()))
                else emit(Resource.Success(createEmptyLocal(vehiculoId)))
            } else {
                emit(Resource.Success(createEmptyLocal(vehiculoId)))
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
            // 1. Agregamos los items (Solo el booleano, según tu curl)
            items.forEach { (key, item) ->
                put(key, item.estado) // "mazoGoma": true
            }
            // 2. Agregamos la ubicación
            put("ubicacion", location)
        }
    }

    // --- EMPTY LOCAL ---
    private fun createEmptyLocal(vehiculoId: Int): SpillKitInspection {
        val labels = mapOf(
            "mazoGoma" to "Mazo de goma",
            "setCunas" to "Set de cuñas, tarugos, tacos...",
            "bandeja" to "Bandeja",
            "barrerasOleofilicas" to "Barreras en Tela Oleofilica...",
            "cintaRoja" to "Cinta de seguridad color rojo",
            "cintaAmarilla" to "Cinta de seguridad color amarillo",
            "bolsasRojas" to "Bolsas de color rojo de tipo industrial",
            "panosOleofilicos" to "Paños oleofilicos de 40 cm x 50 cm",
            "recogedorPlastico" to "Recogedor de plástico",
            "manualContingencia" to "Manual de plan de contingencia",
            "guantesNitrilo" to "Guantes de nitrilo",
            "lenteSeguridad" to "Lente de seguridad",
            "respirador" to "Respirador Doble Cartucho...",
            "trajeTyvek" to "Traje tyvek resistente...",
            "botasPVC" to "Botas de PVC con puntera",
            "maletin" to "Maletín"
        )

        val items = labels.mapValues { (_, label) ->
            SpillKitItem(label, false)
        }

        return SpillKitInspection(null, vehiculoId, null, null, "", items)
    }
}