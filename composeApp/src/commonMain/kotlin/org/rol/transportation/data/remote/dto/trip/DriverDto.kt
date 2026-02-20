package org.rol.transportation.data.remote.dto.trip
import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    val id: Int,
    val dni: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val nombreCompleto: String,
    val email: String? = null,
    val celular: String? = null,
    val numeroLicencia: String? = null,
    val claseLicencia: String? = null,
    val categoriaLicencia: String? = null,
    val fotocheck: List<String> = emptyList(),
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val eliminadoEn: String? = null,
    val rol: String? = null,
    val esPrincipal: Boolean = false
)