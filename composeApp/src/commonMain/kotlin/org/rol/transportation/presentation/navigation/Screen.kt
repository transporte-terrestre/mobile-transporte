package org.rol.transportation.presentation.navigation
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object HomeMenu : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class TripDetail(val tripId: Int, val refreshKey: Long = 0L) : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object DriverDocuments : Screen()

    @Serializable
    data object Notifications : Screen()

    @Serializable
    data class Passengers(val tripId: Int) : Screen()

    @Serializable
    data class TripServices(val tripId: Int) : Screen()

    @Serializable
    data class Checklist(
        val tripId: Int,
        val tipo: String,
        val vehiculoId: Int
    ) : Screen()

    @Serializable
    data class ChecklistItemDetail(
        val tripId: Int,
        val checklistItemId: Int,
        val vehiculoId: Int,
        val itemName: String,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class InspectionSheet(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class DocumentInspection(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class LightsAlarm(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class SeatBelts(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class ToolsInspection(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class FirstAid(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()

    @Serializable
    data class SpillKit(
        val vehiculoId: Int,
        val tripId: Int,
        val tipo: String,
        val vehiculoChecklistDocumentId: Int? = null
    ) : Screen()


}
