package org.rol.transportation.presentation.home_trip_detail_services.register_checkpoint

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.presentation.home_trip_detail_services.components.LocationRegistrationForm

@Composable
fun RegisterCheckpointScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: RegisterCheckpointViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onNavigateBack()
            viewModel.clearMessages()
        }
    }

    LocationRegistrationForm(
        title = "Registrar Punto de Control",
        isLoadingLocation = uiState.isLoading || uiState.isLocationLoading,
        isRegistering = uiState.isRegistering,
        nextStepData = uiState.nextStepData,
        currentLocation = uiState.currentLocation,
        buttonText = "Registrar",
        buttonColor = Color(0xFF2196F3), // Azul
        onNavigateBack = onNavigateBack,
        onSubmit = { horaActual, kilometraje, nombreLugar ->
            viewModel.registerCheckpoint(horaActual, kilometraje, 0, nombreLugar)
        }
    )
}
