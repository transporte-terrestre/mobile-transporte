package org.rol.transportation.data.remote.dto.first_aid

import kotlinx.serialization.Serializable

@Serializable
data class FirstAidDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: FirstAidDocumentDto
)