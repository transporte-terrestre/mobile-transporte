package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class TripChecklistDto(
    val id: Int? = null,
    val viajeId: Int,
    val tipo: String,
    val items: List<ChecklistItemDetailDto>,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null
)