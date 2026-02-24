package org.rol.transportation.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.rol.transportation.domain.model.LocationModel
import platform.CoreLocation.*
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSObject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationDataSource : NSObject(), CLLocationManagerDelegateProtocol {

    private val locationManager = CLLocationManager()
    private val _locationFlow = MutableStateFlow<LocationModel?>(null)

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = 5.0
    }

    actual fun getLocationUpdates(): Flow<LocationModel> {
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        return _locationFlow.filterNotNull()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getLastLocation(): LocationModel? {
        return locationManager.location?.let { location ->
            LocationModel(
                latitude = location.coordinate.useContents { latitude },
                longitude = location.coordinate.useContents { longitude },
                accuracy = location.horizontalAccuracy.toFloat(),
                timestamp = (location.timestamp.timeIntervalSince1970 * 1000).toLong()
            )
        }
    }

    actual fun stopUpdates() {
        locationManager.stopUpdatingLocation()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        _locationFlow.value = LocationModel(
            latitude = location.coordinate.useContents { latitude },
            longitude = location.coordinate.useContents { longitude },
            accuracy = location.horizontalAccuracy.toFloat(),
            timestamp = (location.timestamp.timeIntervalSince1970 * 1000).toLong()
        )
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: platform.Foundation.NSError) {
        println("CoreLocation error: ${didFailWithError.localizedDescription}")
    }

    actual fun isLocationEnabled(): Boolean {
        return CLLocationManager.locationServicesEnabled()
    }

    actual fun openLocationSettings() {
        // En iOS, abrir los settings se debe hacer interactuando con UIApplication.sharedApplication.openURL
        // pero requiere linkear UIKit. Dejamos un print para propósitos de CMP de momento.
        println("Abrir configuración de ubicación en iOS...")
    }
}
