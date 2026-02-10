package org.rol.transportation.domain.model.tools_inspection

data class ToolsInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val criteriaLabels: Map<String, String>, // Guardamos las leyendas aquí (CriterioA = "Sin Grasa", etc)
    val items: Map<String, ToolItem>
)
