package org.rol.transportation.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.rol.transportation.platform.MapView
import org.rol.transportation.presentation.theme.YellowPrimary

@Composable
fun MapViewWithLoading(
    latitude: Double,
    longitude: Double,
    title: String,
    modifier: Modifier = Modifier,
    loadingDuration: Long = 2500L,
    isLiteMode: Boolean = false
) {
    var isLoading by remember { mutableStateOf(loadingDuration > 0L) }

    if (loadingDuration > 0L) {
        LaunchedEffect(Unit) {
            delay(loadingDuration)
            isLoading = false
        }
    }

    Box(modifier = modifier) {
        MapView(
            latitude = latitude,
            longitude = longitude,
            title = title,
            isLiteMode = isLiteMode
        )

        // Loading Overlay over the map area
        if (isLoading) {
            MapLoadingOverlay(modifier = Modifier.matchParentSize())
        }
    }
}

@Composable
fun MapLoadingOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color(0xFFE0E0E0).copy(alpha = 0.3f)) // Light scrim over the Map surface
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = YellowPrimary, 
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Cargando mapa...", 
                    color = YellowPrimary, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
