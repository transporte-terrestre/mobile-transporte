package org.rol.transportation.domain.model.first_aid

data class FirstAidInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val location: String,
    val items: Map<String, FirstAidItem>
)
