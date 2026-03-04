package org.rol.transportation.presentation.home_trip_detail_services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.presentation.home_trip_detail_services.components.EditSegmentDialog
import org.rol.transportation.presentation.theme.YellowPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripServicesScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToMap: (Double, Double) -> Unit,
    onNavigateToRegisterLocation: (String) -> Unit,
    onNavigateToScanPassengerList: (Int) -> Unit,
    viewModel: TripServicesViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadData()
    }

    val factory = rememberPermissionsControllerFactory()
    val permissionsController = remember(factory) { factory.createPermissionsController() }

    BindEffect(permissionsController)

    LaunchedEffect(Unit) {
        viewModel.requestPermissionAndStartLocation(permissionsController)
    }

    if (uiState.editingSegment != null) {
        EditSegmentDialog(
            segment = uiState.editingSegment!!,
            onDismissRequest = { viewModel.closeEditDialog() },
            onConfirm = { request ->
                viewModel.updateSegment(request)
            }
        )
    }

    if (uiState.isDeletingSegment) {
        val lastSegment = uiState.segments.lastOrNull()
        if (lastSegment != null) {
            val tipoUpper = lastSegment.tipo.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeletePrompt() },
                title = { Text("Eliminar Tramo") },
                text = { Text("¿Estás seguro que deseas eliminar el tramo '$tipoUpper'? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteLastSegment() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                    ) { Text("Eliminar", color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDeletePrompt() }) { Text("Cancelar") }
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tramos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (uiState.isAddButtonVisible) {
                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.wrapContentSize(Alignment.BottomEnd)) {
                    FloatingActionButton(
                        onClick = { expanded = true },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, "Agregar Tramo")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        val nextTipo = uiState.nextStepData?.tipo?.lowercase()
                        val (menuLabel, menuIcon, menuTint) = when(nextTipo) {
                            "origen" -> Triple("Iniciar", Icons.Default.PlayArrow, Color(0xFF4CAF50))
                            "destino" -> Triple("Terminar", Icons.Default.Flag, Color(0xFFF44336))
                            else -> Triple("Próximo", Icons.Default.Place, Color(0xFF2196F3))
                        }

                        DropdownMenuItem(
                            text = { Text(menuLabel) },
                            leadingIcon = { Icon(menuIcon, contentDescription = null, tint = menuTint) },
                            onClick = {
                                expanded = false
                                uiState.nextStepData?.tipo?.let { tipo ->
                                    onNavigateToRegisterLocation(tipo)
                                }
                            }
                        )
                        if (uiState.segments.isNotEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Ocasional") },
                                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFFFFA000)) }, // Amber
                                onClick = {
                                    expanded = false
                                    onNavigateToRegisterLocation("parada")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Descanso") },
                                leadingIcon = { Icon(Icons.Default.PauseCircleOutline, contentDescription = null, tint = Color(0xFF795548)) }, // Brown
                                onClick = {
                                    expanded = false
                                    onNavigateToRegisterLocation("descanso")
                                }
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // SEGMENTS SECTION
            Box(Modifier.fillMaxSize()) {
                if (uiState.isLoading && !uiState.showDialog) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else if (uiState.segments.isEmpty()) {
                    Text("No hay tramos registrados", Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(uiState.segments) { index, segment ->
                            val isLastElement = index == uiState.segments.size - 1
                            SegmentCard(
                                segment = segment,
                                isDeletable = isLastElement,
                                onEditClick = { viewModel.openEditDialog(segment) },
                                onDeleteClick = { if (isLastElement) viewModel.promptDeleteLastSegment() },
                                onNavigateToScanPassengerList = onNavigateToScanPassengerList
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentCard(
    segment: SegmentDto,
    isDeletable: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigateToScanPassengerList: (Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val tipoLower = segment.tipo.lowercase()
    val (iconTintColor, iconVector, tipoDisplayName) = when (tipoLower) {
        "origen" -> Triple(Color(0xFF4CAF50), Icons.Default.PlayArrow, "Origen")
        "punto" -> Triple(Color(0xFF2196F3), Icons.Default.Place, "Punto de Control")
        "parada" -> Triple(Color(0xFFFFA000), Icons.Default.Place, "Parada Ocasional")
        "descanso" -> Triple(Color(0xFF795548), Icons.Default.LocalCafe, "Descanso")
        "destino" -> Triple(Color(0xFFF44336), Icons.Default.Flag, "Destino")
        else -> Triple(Color.Gray, Icons.Default.Place, segment.tipo.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
    }

    val iconBgColor = iconTintColor.copy(alpha = if (isDark) 0.2f else 0.1f)
    val cardBgColor = if (isDark) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Icon and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = iconBgColor,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = iconTintColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = segment.nombreLugar ?: tipoDisplayName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = tipoDisplayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = iconTintColor
                    )
                    
                    if (tipoLower != "descanso" && tipoLower != "destino") {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black, // Fondo oscuro
                            modifier = Modifier.clickable { onNavigateToScanPassengerList(segment.id) },
                            border = BorderStroke(1.dp, Color(0xFF333333)) // Pequeño borde para que no se pierda en el modo oscuro
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = "Pasajeros",
                                    tint = YellowPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${segment.numeroPasajeros ?: 0} pas",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = YellowPrimary
                                )
                            }
                        }
                    }
                }
            }
            
            // Right Side: Time, KM
            Column(horizontalAlignment = Alignment.End) {
                val timeString = segment.horaFinal?.substringAfter("T")?.take(5) ?: "--:--"
                Text(
                    text = timeString,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (tipoLower != "descanso") {
                    Text(
                        text = "${segment.kilometrajeFinal ?: 0.0} KM",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    if (isDeletable) {
                        IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}