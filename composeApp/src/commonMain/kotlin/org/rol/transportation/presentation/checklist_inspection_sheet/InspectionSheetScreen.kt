package org.rol.transportation.presentation.checklist_inspection_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.domain.model.inspection_sheet.InspectionItem
import org.rol.transportation.domain.model.inspection_sheet.InspectionSection
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionSheetScreen(
    vehiculoId: Int,
    tripId: Int,
    tipo: String,
    vehiculoChecklistDocumentId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: InspectionSheetViewModel = koinViewModel {
        parametersOf(vehiculoId, tripId, tipo, vehiculoChecklistDocumentId)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Diálogo de éxito
    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.clearSuccessMessage()
                onNavigateBack()
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(text = "¡Éxito!", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = uiState.successMessage ?: "",
                    textAlign = TextAlign.Center
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearSuccessMessage()
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Hoja de Inspección",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (uiState.hasChanges && !uiState.isLoading) {
                Surface(
                    shadowElevation = 8.dp,
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = viewModel::saveInspectionSheet,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !uiState.isSaving,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Guardando...", fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Rounded.Save, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                uiState.error != null && uiState.hojaInspeccion == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Ocurrió un error",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.error ?: "",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }

                uiState.hojaInspeccion != null -> {
                    InspectionSheetContent(
                        inspectionSheet = uiState.hojaInspeccion!!,
                        onItemValueChanged = viewModel::onItemValueChanged,
                        error = uiState.error,
                        onClearError = viewModel::clearError
                    )
                }

                else -> {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text("Cargando formulario...", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun InspectionSheetContent(
    inspectionSheet: InspectionSheet,
    onItemValueChanged: (String, String, Boolean) -> Unit,
    error: String?,
    onClearError: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mensaje de error
        error?.let {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Error, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(onClick = onClearError) {
                        Icon(Icons.Rounded.Close, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }

        // Secciones
        InspectionSectionTable(
            section = inspectionSheet.document.declaracionJurada,
            sectionKey = "declaracionJurada",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.estadoGeneral,
            sectionKey = "estadoGeneral",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.estadoInterno,
            sectionKey = "estadoInterno",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.elementosSeguridad,
            sectionKey = "elementosSeguridad",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.estadoMecanico,
            sectionKey = "estadoMecanico",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.sistemasCriticos,
            sectionKey = "sistemasCriticos",
            onItemValueChanged = onItemValueChanged
        )

        InspectionSectionTable(
            section = inspectionSheet.document.cinturonesSeguridad,
            sectionKey = "cinturonesSeguridad",
            onItemValueChanged = onItemValueChanged
        )

        // Espacio inferior para el botón flotante
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun InspectionSectionTable(
    section: InspectionSection,
    sectionKey: String,
    onItemValueChanged: (String, String, Boolean) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (!isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Encabezado de la sección
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Assignment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = section.label.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    letterSpacing = 1.sp
                )
            }

            // Tabla de items
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                section.items.entries.forEachIndexed { index, (key, item) ->
                    InspectionTableRow(
                        item = item,
                        isLastItem = index == section.items.size - 1,
                        onValueChanged = { newValue ->
                            onItemValueChanged(sectionKey, key, newValue)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InspectionTableRow(
    item: InspectionItem,
    isLastItem: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val colorIndicator = when (item.color.lowercase()) {
        "rojo" -> Color(0xFFEF5350)
        "amarillo" -> Color(0xFFFFA726)
        "verde" -> Color(0xFF66BB6A)
        else -> MaterialTheme.colorScheme.outline
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onValueChanged(!item.value) }
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de color
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colorIndicator)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Texto del item
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Checkbox
            Checkbox(
                checked = item.value,
                onCheckedChange = onValueChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // Divider entre filas
        if (!isLastItem) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                thickness = 1.dp,
                color = if (isDark) {
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                }
            )
        }
    }
}