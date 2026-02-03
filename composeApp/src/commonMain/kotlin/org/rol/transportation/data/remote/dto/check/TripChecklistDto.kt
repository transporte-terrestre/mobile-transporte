package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class TripChecklistDto(
    val id: Int,
    val viajeId: Int,
    val tipo: String,
    val validadoPor: Int,
    val validadoEn: String,
    val observaciones: String? = null,
    val creadoEn: String,
    val actualizadoEn: String,
    val items: List<ChecklistItemDetailDto>,
    val message: String? = null
)