package org.rol.transportation.domain.model.enums

enum class TripStatus(
    val displayName: String,
    val color: Long,
) {
    PROGRAMADO("Programado", 0xFFFFC107),
    EN_PROGRESO("En Progreso", 0xFF2196F3),
    COMPLETADO("Completado", 0xFF4CAF50),
    CANCELADO("Cancelado", 0xFFF44336);

    companion object {
        fun fromString(value: String?): TripStatus {
            return when (value?.lowercase()) {
                "programado" -> PROGRAMADO
                "en_progreso", "en progreso" -> EN_PROGRESO
                "completado" -> COMPLETADO
                "cancelado" -> CANCELADO
                else -> PROGRAMADO
            }
        }
    }
}