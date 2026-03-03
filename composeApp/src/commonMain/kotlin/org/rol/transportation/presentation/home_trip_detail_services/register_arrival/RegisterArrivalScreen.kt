package org.rol.transportation.presentation.home_trip_detail_services.register_arrival

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.presentation.home_trip_detail_services.components.LocationRegistrationForm

@Composable
fun RegisterArrivalScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: RegisterArrivalViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onNavigateBack()
            viewModel.clearMessages()
        }
    }

    LocationRegistrationForm(
        title = "Registrar Llegada",
        isLoadingLocation = uiState.isLocationLoading,
        isRegistering = uiState.isRegistering,
        nextStepData = uiState.nextStepData,
        currentLocation = uiState.currentLocation,
        buttonText = "Terminar",
        buttonColor = Color(0xFFF44336), // Rojo
        onNavigateBack = onNavigateBack,
        onSubmit = { horaActual, kilometraje, nombreLugar ->
            viewModel.registerArrival(horaActual, kilometraje, 0, nombreLugar)
        }
    )
}
