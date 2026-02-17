package org.rol.transportation.presentation.home_trip_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.domain.model.enums.TripStatus
import org.rol.transportation.presentation.theme.GrayText
import org.rol.transportation.presentation.theme.TransportationTheme
import org.rol.transportation.presentation.theme.YellowPrimary
import org.rol.transportation.utils.DateFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    tripId: Int,
    refreshTrigger: Long = 0L,
    onNavigateBack: () -> Unit,
    onNavigateToChecklist: (tripId: Int, tipo: String, vehiculoId: Int) -> Unit,
    onNavigateToPassengers: (tripId: Int) -> Unit,
    onNavigateToTripServices: (tripId: Int) -> Unit,
    viewModel: TripDetailViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()

    // refrescar cuando cambia refreshTrigger
    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger > 0) {
            viewModel.refreshData()
        }
    }

    // lifecycle para la primera carga
    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Viaje",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge) },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar el viaje",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }

                uiState.viaje != null -> {
                    TripDetailContent(trip = uiState.viaje!!,
                        onNavigateToChecklist = onNavigateToChecklist,
                        onNavigateToPassengers = onNavigateToPassengers,
                        onNavigateToTripServices = onNavigateToTripServices
                    )
                }
            }
        }
    }
}


@Composable
private fun TripDetailContent(
    trip: Trip,
    onNavigateToChecklist: (tripId: Int, tipo: String, vehiculoId: Int) -> Unit,
    onNavigateToPassengers: (tripId: Int) -> Unit,
    onNavigateToTripServices: (tripId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusBanner(trip.estado)

        // CLIENTE: Ahora con header gris y datos verticales
        DetailSectionCard(title = "CLIENTE", icon = Icons.Default.Person) {
            UserInfoVertical(
                name = trip.cliente.nombreCompleto,
                imageUrl = trip.cliente.imagenes.firstOrNull(),
                label1 = "EMAIL",
                value1 = trip.cliente.email ?: "N/A",
                label2 = "TELÉFONO",
                value2 = trip.cliente.telefono ?: "N/A"
            )
        }

        // CONDUCTOR: Ahora con header gris
        val conductor = trip.conductores.firstOrNull { it.esPrincipal }
        DetailSectionCard(title = "CONDUCTOR", icon = Icons.Default.Badge, badge = "Principal") {
            UserInfoVertical(
                name = conductor?.nombreCompleto ?: "N/A",
                imageUrl = conductor?.fotocheck?.firstOrNull(),
                label1 = "LICENCIA",
                value1 = conductor?.numeroLicencia ?: "N/A",
                label2 = "ROL",
                value2 = conductor?.rol?.uppercase() ?: "N/A"
            )
        }

        // RUTA: Mantiene su estructura dinámica
        trip.ruta?.let { ruta ->
            DetailSectionCard(title = "RUTA", icon = Icons.Default.GpsFixed) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Origen", style = MaterialTheme.typography.labelSmall, color = GrayText)
                        Text(ruta.origen, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("${ruta.origenLat}, ${ruta.origenLng}", style = MaterialTheme.typography.bodySmall, color = GrayText)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = YellowPrimary, modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 8.dp))
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("Destino", style = MaterialTheme.typography.labelSmall, color = GrayText)
                        Text(ruta.destino, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("${ruta.destinoLat}, ${ruta.destinoLng}", style = MaterialTheme.typography.bodySmall, color = GrayText)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Text("Distancia Estimada", style = MaterialTheme.typography.labelSmall, color = GrayText)
                Text("${trip.distanciaEstimada} km", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        // VEHÍCULO
        val vehiculo = trip.vehiculos.firstOrNull { it.esPrincipal }
        vehiculo?.let { v ->
            DetailSectionCard(title = "VEHÍCULO", icon = Icons.Default.DirectionsCar) {
                v.imagenes.firstOrNull()?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Foto del vehículo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(12.dp))
                }

                InfoLabel(label = "Marca y Modelo", value = "${v.marca} ${v.modelo}")
                InfoLabel(label = "Placa", value = v.placa)
                InfoLabel(label = "Año", value = v.anio.toString())
            }
        }

        // CRONOGRAMA
        DetailSectionCard(title = "CRONOGRAMA", icon = Icons.Default.Schedule) {
            TimelineItem(
                label = "SALIDA ESTIMADA",
                time = DateFormatter.formatOnlyTime(trip.fechaSalida),
                date = DateFormatter.formatOnlyDate(trip.fechaSalida),
                isStart = true
            )
            trip.fechaLlegada?.let { llegada ->
                TimelineItem(
                    label = "LLEGADA REAL",
                    time = DateFormatter.formatOnlyTime(llegada),
                    date = DateFormatter.formatOnlyDate(llegada),
                    isStart = false
                )
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToPassengers(trip.id) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ver Lista de Pasajeros",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToTripServices(trip.id) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Segment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Abrir Detalle de Tramos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val vehiculoId = trip.vehiculos.firstOrNull { it.esPrincipal }?.id ?: 0

                val bgDepartureButton = if (trip.checkInSalida)
                    TransportationTheme.myColors.bgArrival
                else
                    TransportationTheme.myColors.bgDeparture

                val textDepartureButton = if (trip.checkInSalida)
                    TransportationTheme.myColors.textArrival
                else
                    TransportationTheme.myColors.textDeparture

                // Si checkInLlegada es TRUE -> Verde
                // Si checkInLlegada es FALSE -> Azul
                val bgArrivalButton = if (trip.checkInLlegada)
                    TransportationTheme.myColors.bgArrival
                else
                    TransportationTheme.myColors.bgDeparture

                val textArrivalButton = if (trip.checkInLlegada)
                    TransportationTheme.myColors.textArrival
                else
                    TransportationTheme.myColors.textDeparture

                ActionButton(
                    text = "Check Salida",
                    icon = Icons.Rounded.AssignmentTurnedIn,
                    backgroundColor = bgDepartureButton,
                    contentColor = textDepartureButton,
                    onClick = { onNavigateToChecklist(trip.id, ChecklistType.SALIDA.value, vehiculoId) },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    text = "Check Llegada",
                    icon = Icons.Rounded.Flag,
                    backgroundColor = bgArrivalButton,
                    contentColor = textArrivalButton,
                    onClick = { onNavigateToChecklist(trip.id, ChecklistType.LLEGADA.value, vehiculoId) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}


// Tarjeta con Cabecera para todas las secciones
@Composable
fun DetailSectionCard(
    title: String,
    icon: ImageVector,
    badge: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (!isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                badge?.let {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}


// Componente para el Banner de Estado Amarillo
@Composable
fun StatusBanner(status: TripStatus) {
    val statusColor = Color(status.color)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = statusColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Text("ESTADO", fontWeight = FontWeight.Black, color = Color.White)
            }
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.2f)
            ) {
                Text(
                    text = status.displayName.uppercase(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Componente para items de Horario (Timeline)
@Composable
fun TimelineItem(label: String, time: String, date: String, isStart: Boolean) {
    val isPm = time.contains("PM", ignoreCase = true)
    val timeClean = time.replace("AM", "").replace("PM", "").trim()

    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = if (isStart) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                modifier = Modifier.padding(12.dp),
                tint = if (isSystemInDarkTheme()) GrayText else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = GrayText)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(timeClean, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = YellowPrimary)
                Text(
                    text = if (isPm) " PM" else " AM",
                    style = MaterialTheme.typography.labelMedium,
                    color = YellowPrimary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Text(date, style = MaterialTheme.typography.bodySmall, color = GrayText)
        }
    }
}

// Componente para etiquetas simples (Usado en Vehículo)
@Composable
fun InfoLabel(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun UserInfoVertical(
    name: String,
    imageUrl: String?,
    label1: String,
    value1: String,
    label2: String,
    value2: String
) {
    Row(verticalAlignment = Alignment.Top) {
        // Avatar con imagen
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Foto de Usuario",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Person),
                error = rememberVectorPainter(Icons.Default.Error)
            )
        } else {
            // Placeholder si no hay imagen
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(54.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text("NOMBRE COMPLETO", style = MaterialTheme.typography.labelSmall, color = GrayText)
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(12.dp))

            Text(label1, style = MaterialTheme.typography.labelSmall, color = GrayText)
            Text(value1, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

            Spacer(Modifier.height(12.dp))

            Text(label2, style = MaterialTheme.typography.labelSmall, color = GrayText)
            Text(value2, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
