package org.rol.transportation.data.remote.dto.trip
import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    val id: Int,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String? = null,
    val celular: String? = null,
    val numeroLicencia: String,
    val claseLicencia: String,
    val categoriaLicencia: String,
    val fotocheck: List<String> = emptyList(),
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null,
    val rol: String,
    val esPrincipal: Boolean
)