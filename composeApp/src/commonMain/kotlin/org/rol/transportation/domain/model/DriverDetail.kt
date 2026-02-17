package org.rol.transportation.domain.model

data class DriverDetail(
    val id: Int,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String,
    val celular: String?,
    val numeroLicencia: String,
    val documentos: List<DriverDocument>
)

data class DriverDocument(
    val id: Int,
    val tipo: String,
    val nombre: String,
    val url: String,
    val fechaExpiracion: String?,
    val fechaEmision: String?
)
