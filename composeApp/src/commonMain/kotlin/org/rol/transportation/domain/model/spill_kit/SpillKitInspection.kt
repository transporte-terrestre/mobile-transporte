package org.rol.transportation.domain.model.spill_kit

data class SpillKitInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val location: String,
    val items: Map<String, SpillKitItem>
)
