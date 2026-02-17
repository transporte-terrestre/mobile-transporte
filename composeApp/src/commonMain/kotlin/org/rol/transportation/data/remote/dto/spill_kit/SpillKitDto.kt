package org.rol.transportation.data.remote.dto.spill_kit

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class SpillKitDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: JsonObject // Trae items Y un String "ubicacion" mezclados
)
