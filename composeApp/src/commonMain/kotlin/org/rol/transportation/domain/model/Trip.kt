package org.rol.transportation.domain.model

import org.rol.transportation.domain.model.enums.TripStatus

data class Trip(
    val id: Int,
    val cliente: Customer,
    val ruta: Route?,
    val conductores: List<Driver>,
    val vehiculos: List<Vehicle>,
    val estado: TripStatus,
    val fechaSalida: String,
    val fechaLlegada: String?,
    val distanciaEstimada: String,
    val distanciaFinal: String?,
    val modalidadServicio: String,
    val tripulantes: List<String>
)

