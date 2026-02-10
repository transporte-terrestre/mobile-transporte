package org.rol.transportation.data.remote.dto.first_aid

import kotlinx.serialization.Serializable

@Serializable
data class FirstAidDto(
    val viajeId: Int,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: FirstAidDocumentDto
)