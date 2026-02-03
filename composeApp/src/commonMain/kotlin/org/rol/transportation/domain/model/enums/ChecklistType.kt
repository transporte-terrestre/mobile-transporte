package org.rol.transportation.domain.model.enums

enum class ChecklistType(val value: String, val displayName: String) {
    SALIDA("salida", "Check Salida"),
    LLEGADA("llegada", "Check Llegada");

    companion object {
        fun fromString(value: String): ChecklistType {
            return entries.find { it.value == value } ?: SALIDA
        }
    }
}