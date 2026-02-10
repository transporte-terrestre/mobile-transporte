package org.rol.transportation.domain.model

data class ChecklistDocument(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: DocumentContent
)
