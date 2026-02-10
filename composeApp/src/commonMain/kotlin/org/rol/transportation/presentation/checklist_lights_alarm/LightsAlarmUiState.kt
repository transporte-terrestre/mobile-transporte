package org.rol.transportation.presentation.checklist_lights_alarm

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.lights_alarm.LightsAlarmInspection

data class LightsAlarmUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val inspection: LightsAlarmInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)