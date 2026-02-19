package org.rol.transportation.domain.model.driver_documents

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
