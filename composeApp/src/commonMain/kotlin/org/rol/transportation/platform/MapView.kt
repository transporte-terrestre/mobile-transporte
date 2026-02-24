package org.rol.transportation.platform

import androidx.compose.runtime.Composable

@Composable
expect fun MapView(
    latitude: Double,
    longitude: Double,
    title: String
)
