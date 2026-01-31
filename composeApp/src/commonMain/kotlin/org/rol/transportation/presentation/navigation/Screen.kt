package org.rol.transportation.presentation.navigation
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class TripDetail(val tripId: Int) : Screen()

    @Serializable
    data object Profile : Screen()
}