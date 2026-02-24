package org.rol.transportation.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.foundation.layout.fillMaxSize
import cocoapods.GoogleMaps.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    latitude: Double,
    longitude: Double,
    title: String
) {
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val camera = GMSCameraPosition.cameraWithLatitude(
                latitude = latitude,
                longitude = longitude,
                zoom = 15f
            )
            val mapView = GMSMapView(frame = kotlinx.cinterop.CGRectMake(0.0, 0.0, 0.0, 0.0))
            mapView.camera = camera

            // Marcador en la ubicación
            val marker = GMSMarker()
            marker.position = CLLocationCoordinate2DMake(latitude, longitude)
            marker.title = title
            marker.map = mapView

            mapView
        }
    )
}
