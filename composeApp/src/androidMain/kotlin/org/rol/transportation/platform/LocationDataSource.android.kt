package org.rol.transportation.platform

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.rol.transportation.domain.model.LocationModel
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationDataSource actual constructor() {

    private val context: Context get() = AppContext.get() as Context

    private val fusedClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private var locationCallback: LocationCallback? = null

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000L
    ).setMinUpdateIntervalMillis(3000L).build()

    @SuppressLint("MissingPermission")
    actual fun getLocationUpdates(): Flow<LocationModel> = callbackFlow {
        Log.d("GPS_DEBUG", "Iniciando getLocationUpdates() en DataSource")
        
        fusedClient.lastLocation.addOnSuccessListener { location ->
            Log.d("GPS_DEBUG", "lastLocation obtenido rápidamente: $location")
            location?.let { trySend(it.toLocationModel()) }
        }.addOnFailureListener {
            Log.d("GPS_DEBUG", "lastLocation falló: ${it.message}")
        }

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Log.d("GPS_DEBUG", "onLocationResult llamado. Resolvió: ${result.lastLocation}")
                result.lastLocation?.let { trySend(it.toLocationModel()) }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                super.onLocationAvailability(availability)
                Log.d("GPS_DEBUG", "onLocationAvailability: isLocationAvailable=${availability.isLocationAvailable}")
            }
        }
        locationCallback = callback

        Log.d("GPS_DEBUG", "Solicitando requestLocationUpdates al FusedClient...")
        fusedClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            Log.d("GPS_DEBUG", "requestLocationUpdates registrado con SUCCESS")
        }.addOnFailureListener { e ->
            Log.d("GPS_DEBUG", "Error requestLocationUpdates: ${e.message}")
        }

        awaitClose { 
            Log.d("GPS_DEBUG", "Se cerró el Flow, llamando a removeLocationUpdates")
            fusedClient.removeLocationUpdates(callback) 
        }
    }

    @SuppressLint("MissingPermission")
    actual suspend fun getLastLocation(): LocationModel? =
        suspendCancellableCoroutine { cont ->
            fusedClient.lastLocation
                .addOnSuccessListener { cont.resume(it?.toLocationModel()) }
                .addOnFailureListener { cont.resume(null) }
        }

    actual fun stopUpdates() {
        locationCallback?.let { fusedClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    actual fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    actual fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun Location.toLocationModel() = LocationModel(
        latitude = latitude,
        longitude = longitude,
        accuracy = accuracy,
        timestamp = time
    )
}
