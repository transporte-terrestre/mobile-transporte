package org.rol.transportation.data.remote.dto.driver_document

import kotlinx.serialization.Serializable

@Serializable
data class DriverDetailResponse(
    val id: Int,
    val tipoDocumento: String? = null,
    val nacionalidad: String? = null,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String,
    val celular: String? = null,
    val numeroLicencia: String,
    val claseLicencia: String? = null,
    val categoriaLicencia: String? = null,
    val fotocheck: List<String> = emptyList(),
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val eliminadoEn: String? = null,
    val documentos: Map<String, List<DocumentDto>> = emptyMap()
)
