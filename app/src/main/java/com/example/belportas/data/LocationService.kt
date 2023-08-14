package com.example.belportas.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.TravelMode
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import java.io.IOException

@Suppress("DEPRECATION")
class LocationService(private val context: Context) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 2 * 60 * 1000
        fastestInterval = 1 * 60 * 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val geoApiContext = GeoApiContext.Builder()
        .apiKey("AIzaSyAgu6ypHGVEpzNdqQ6Ce2o_TO1p69ooJgI")
        .build()

    private val calculateDistanceSemaphore = Semaphore(1)

    private val distanceCache = mutableMapOf<String, Deferred<Long>>()

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(callback: (Location) -> Unit) {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val lastLocation = locationResult.lastLocation
                    lastLocation?.let { callback(it) }
                }
            },
            Looper.getMainLooper()
        )
    }
    suspend fun calculateDistanceAsync(origin: Location, destinationAddress: String, numberNote:String): Deferred<Long> =
        coroutineScope {
            distanceCache[destinationAddress] ?: async {
                calculateDistanceSemaphore.acquire()
                try {
                    val result = withContext(Dispatchers.IO) {
                        DirectionsApi.newRequest(geoApiContext)
                            .mode(TravelMode.DRIVING)
                            .origin("${origin.latitude},${origin.longitude}")
                            .destination(destinationAddress)
                            .await()
                    }
                    val distance = result.routes[0].legs[0].distance.inMeters / 1000
                    distance
                } catch (e: IOException) {
                    Log.e("LocationService", "Error in connection", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Tarefa $numberNote não adicionada por erro de conexão, tente novamente com acesso a internet !" , Toast.LENGTH_SHORT).show()
                        Log.e("Connection error:", e.message ?: "No error message")
                    }
                    -1L
                } catch (e: ZeroResultsException) {
                    Log.e("LocationService", "No route found between origin and destination", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "No route found between origin and destination", Toast.LENGTH_SHORT).show()
                    }
                    Long.MAX_VALUE
                } catch (e: Exception) {
                    Log.e("LocationService", "Error calculating distance", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error calculating distance: ${e.message ?: "No error message"}", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                    Long.MAX_VALUE
                } finally {
                    calculateDistanceSemaphore.release()
                }
            }.also { deferredResult ->
                if (deferredResult.await() > 0 && deferredResult.await() != Long.MAX_VALUE) {
                    distanceCache[destinationAddress] = deferredResult
                }
            }
        }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationAsync(): Deferred<Location> = coroutineScope {
        val locationDeferred = CompletableDeferred<Location>()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val lastLocation = locationResult.lastLocation

                    lastLocation?.let {
                        fusedLocationProviderClient.removeLocationUpdates(this)
                        locationDeferred.complete(it)
                    }
                }
            },
            Looper.getMainLooper()
        )

        locationDeferred
    }
}
