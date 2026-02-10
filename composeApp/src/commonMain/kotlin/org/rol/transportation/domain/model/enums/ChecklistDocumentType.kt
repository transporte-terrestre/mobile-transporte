package org.rol.transportation.domain.model.enums

enum class ChecklistDocumentType(val endpoint: String) {
    IPERC_CONTINUO("iperc-continuo"),
    HOJA_INSPECCION("hoja-inspeccion"),
    INSPECCION_DOCUMENTOS("inspeccion-documentos"),
    LUCES_ALARMA("luces-alarma"),
    SEAT_BELTS("cinturones-seguridad"),
    TOOLS_INSPECTION("inspeccion-herramientas"),
    FIRST_AID("inspeccion-botiquines"),
    SPILL_KIT("kit-antiderrames"),
    REVISION_VEHICULOS("revision-vehiculos");

    companion object {
        fun fromChecklistItemId(itemId: Int): ChecklistDocumentType? {
            return when (itemId) {
                1 -> IPERC_CONTINUO
                2 -> HOJA_INSPECCION
                3 -> INSPECCION_DOCUMENTOS
                4 -> LUCES_ALARMA
                5 -> SEAT_BELTS
                6 -> TOOLS_INSPECTION
                7 -> FIRST_AID
                8 -> SPILL_KIT
                9 -> REVISION_VEHICULOS
                else -> null
            }
        }
    }
}