package org.rol.transportation.domain.model

data class Passenger(
    val id: Int,
    val viajeId: Int,
    val pasajeroId: Int?,
    val dni: String,
    val nombreCompleto: String,
    val asistencia: Boolean,
    val esTramoActual: Boolean
)
