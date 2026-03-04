package org.rol.transportation.presentation.home_trip_detail_services.register_departure

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.presentation.home_trip_detail_services.components.LocationRegistrationForm

@Composable
fun RegisterDepartureScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: RegisterDepartureViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onNavigateBack() // Opcional: mostrar un toast antes
            viewModel.clearMessages()
        }
    }

    LocationRegistrationForm(
        title = "Registrar Salida",
        isLoadingLocation = uiState.isLoading || uiState.isLocationLoading,
        isRegistering = uiState.isRegistering,
        nextStepData = uiState.nextStepData,
        currentLocation = uiState.currentLocation,
        buttonText = "Iniciar",
        buttonColor = Color(0xFF4CAF50), // Verde
        onNavigateBack = onNavigateBack,
        onSubmit = { horaActual, kilometraje, nombreLugar ->
            viewModel.registerDeparture(horaActual, kilometraje, 0, nombreLugar)
        }
    )
}
