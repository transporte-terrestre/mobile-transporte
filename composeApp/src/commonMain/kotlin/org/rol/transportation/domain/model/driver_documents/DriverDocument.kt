package org.rol.transportation.domain.model.driver_documents

data class DriverDocument(
    val id: Int,
    val tipo: String,
    val nombre: String,
    val url: String,
    val fechaExpiracion: String?,
    val fechaEmision: String?
)