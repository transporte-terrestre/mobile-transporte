package org.rol.transportation.data.remote.dto.trip
import kotlinx.serialization.Serializable

@Serializable
data class CustomerDto (
    val id: Int,
    val tipoDocumento: String,
    val dni: String? = null,
    val ruc: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val razonSocial: String? = null,
    val nombreCompleto: String,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val horasContrato: String,
    val imagenes: List<String> = emptyList(),
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null
)