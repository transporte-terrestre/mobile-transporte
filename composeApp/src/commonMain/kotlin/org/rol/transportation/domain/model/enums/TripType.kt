package org.rol.transportation.domain.model.enums

enum class TripType(val value: String) {
    SALIDA("salida"),
    LLEGADA("llegada");

    companion object {
        fun fromString(value: String?): TripType? {
            return when (value?.lowercase()) {
                "salida" -> SALIDA
                "llegada" -> LLEGADA
                else -> null
            }
        }
    }
}