package org.rol.transportation.data.remote.dto.driver_document

import kotlinx.serialization.Serializable

@Serializable
data class DocumentDto(
    val id: Int,
    val conductorId: Int,
    val tipo: String,
    val nombre: String,
    val url: String,
    val fechaExpiracion: String? = null,
    val fechaEmision: String? = null,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null
)