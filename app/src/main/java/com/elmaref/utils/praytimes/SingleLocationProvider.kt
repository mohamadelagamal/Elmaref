package com.elmaref.utils.praytimes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

object SingleLocationProvider {

        // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
        // contents of the else and if. Also be sure to check gps permission/settings are allowed.
        // call usually takes <10ms

    // requestSingleUpdate is used to get the current location of the user and pass it to the onSuccess callback function
        @SuppressLint("MissingPermission", "CheckResult")
        fun requestSingleUpdate(context: Context, onSuccess:(GPSCoordinates)->Unit){
            // mFusedLocationClient is a FusedLocationProviderClient object that is used to get the current location of the user
            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        // locationRequest is a LocationRequest object that is used to set the priority and interval of the location updates
            val locationRequest = LocationRequest.create()
        // setting the priority of the location request to high accuracy
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // setting the interval of the location request to 20 seconds
            locationRequest.interval = 20 * 1000
        // setting the fastest interval of the location request to 10 seconds
            var locationCallback: LocationCallback? = null

        // locationCallback is a LocationCallback object that is used to get the current location of the user
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            onSuccess(GPSCoordinates(location.latitude,location.longitude))
                            locationCallback?.let { mFusedLocationClient.removeLocationUpdates(it) }
                        }
                    }
                }
            }
        // requestLocationUpdates is used to request the location updates
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }


        // consider returning Location instead of this dummy wrapper class
        class GPSCoordinates(theLatitude: Double, theLongitude: Double) {
            var longitude = -1f
            var latitude = -1f

            init {
                longitude = theLongitude.toFloat()
                latitude = theLatitude.toFloat()
            }
        }
    }


