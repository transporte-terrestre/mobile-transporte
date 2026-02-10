package org.rol.transportation.data.remote.dto.first_aid

import kotlinx.serialization.Serializable

@Serializable
data class FirstAidItemDto(
    val label: String,
    val habilitado: Boolean,
    val fechaVencimiento: String?,
    val fechaSalida: String?,
    val fechaReposicion: String?
)