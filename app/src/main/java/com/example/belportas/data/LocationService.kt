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

    suspend fun calculateDistanceAsync(origin: Location, destinationAddress: String): Deferred<Long> =
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
                    result.routes[0].legs[0].distance.inMeters / 1000
                } catch (e: ZeroResultsException) {
                    Log.e("LocationService", "No route found between origin and destination", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Nenhum caminho encontrado entre origem e destino", Toast.LENGTH_SHORT).show()
                    }
                    Long.MAX_VALUE
                } catch (e: Exception) {
                    Log.e("LocationService", "Error calculating distance", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro ao calcular a distância: ${e.message ?: "No error message"}", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                    Long.MAX_VALUE
                } finally {
                    calculateDistanceSemaphore.release()
                }
            }.also {
                distanceCache[destinationAddress] = it
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
