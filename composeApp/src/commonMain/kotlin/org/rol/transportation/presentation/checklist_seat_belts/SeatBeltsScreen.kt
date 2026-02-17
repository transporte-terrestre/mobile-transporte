package org.rol.transportation.presentation.checklist_seat_belts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.domain.model.seat_belts.SeatBeltItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatBeltsScreen(
    vehiculoId: Int,
    tripId: Int,
    tipo: String,
    vehiculoChecklistDocumentId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: SeatBeltsViewModel = koinViewModel {
        parametersOf(vehiculoId, tripId, tipo, vehiculoChecklistDocumentId ?: -1)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }

    // Dialogs state
    var showObsDialog by remember { mutableStateOf(false) }
    var currentKey by remember { mutableStateOf("") }
    var currentLabel by remember { mutableStateOf("") }
    var currentText by remember { mutableStateOf("") }

    // Success Dialog
    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages(); onNavigateBack() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary) },
            title = { Text("¡Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text(uiState.successMessage ?: "") },
            confirmButton = { Button(onClick = { viewModel.clearMessages(); onNavigateBack() }) { Text("Aceptar") } }
        )
    }

    // Observation Dialog
    if (showObsDialog) {
        AlertDialog(
            onDismissRequest = { showObsDialog = false },
            title = { Text("Observación - $currentLabel", style = MaterialTheme.typography.titleMedium) },
            text = {
                OutlinedTextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    label = { Text("Detalles") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onObservationChanged(currentKey, currentText)
                    showObsDialog = false
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { showObsDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cinturones de Seguridad", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        bottomBar = {
            if (uiState.hasChanges && !uiState.isLoading) {
                Surface(shadowElevation = 8.dp) {
                    Row(Modifier.padding(16.dp)) {
                        Button(
                            onClick = viewModel::save,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            enabled = !uiState.isSaving
                        ) {
                            if (uiState.isSaving) CircularProgressIndicator(color = Color.White) else Text("GUARDAR CAMBIOS")
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.inspection != null -> {
                    SeatBeltsContent(
                        items = uiState.inspection!!.items,
                        onItemChanged = viewModel::onItemChanged,
                        onEditObservation = { key, label, text ->
                            currentKey = key
                            currentLabel = label
                            currentText = text
                            showObsDialog = true
                        }
                    )
                }
                else -> Text("Cargando formulario...", Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun SeatBeltsContent(
    items: Map<String, SeatBeltItem>,
    onItemChanged: (String, Boolean) -> Unit,
    onEditObservation: (String, String, String) -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Chair, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.width(8.dp))
                    Text("ESTADO DE CINTURONES", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }

                Column(Modifier.padding(8.dp)) {
                    items.entries.forEachIndexed { index, (key, item) ->
                        SeatBeltRow(
                            label = item.label,
                            isChecked = item.habilitado,
                            observation = item.observacion,
                            isLastItem = index == items.size - 1,
                            onValueChanged = { onItemChanged(key, it) },
                            onEditObservation = { onEditObservation(key, item.label, item.observacion) }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun SeatBeltRow(
    label: String,
    isChecked: Boolean,
    observation: String,
    isLastItem: Boolean,
    onValueChanged: (Boolean) -> Unit,
    onEditObservation: () -> Unit
) {
    val hasObs = observation.isNotBlank()
    val colorIndicator = Color(0xFF42A5F5)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onValueChanged(!isChecked) }.padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(colorIndicator))
            Spacer(Modifier.width(12.dp))

            Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))

            IconButton(onClick = onEditObservation) {
                Icon(
                    imageVector = if (hasObs) Icons.AutoMirrored.Rounded.Note else Icons.AutoMirrored.Rounded.Note,
                    contentDescription = "Nota",
                    tint = if (hasObs) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                )
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = onValueChanged,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
        }

        if (hasObs) {
            Text("Obs: $observation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 28.dp, bottom = 8.dp), maxLines = 2)
        }

        if (!isLastItem) HorizontalDivider(Modifier.padding(horizontal = 8.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}