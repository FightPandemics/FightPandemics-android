package com.fightpandemics.core.widgets

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fightpandemics.core.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.RuntimeExecutionException
import timber.log.Timber

/*
* created by Osaigbovo Odiase & Jose Li
* */
open class BaseLocationFragment : Fragment() {

    private val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(10 * 1000) // 10 seconds
        .setFastestInterval(5 * 1000) // 5 seconds

    var mFusedLocationClient: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create a FusedLocation Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onDestroyView() {
        if (locationCallback != null) {
            mFusedLocationClient!!.removeLocationUpdates(locationCallback!!)
            locationCallback = null
            // locationRequest
        }
        mFusedLocationClient = null

        super.onDestroyView()
    }

    fun getCurrentLocation() {
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )
            == PackageManager.PERMISSION_GRANTED
        ) {
            val REQUEST_CHECK_STATE = 12300 // any suitable ID
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client = LocationServices.getSettingsClient(requireContext())
            client.checkLocationSettings(builder.build()).addOnCompleteListener { task ->
                try {
                    task.result!!.locationSettingsStates
                    Timber.i("My filters : gps is on")
                } catch (e: RuntimeExecutionException) {
                    Timber.i("My filters : runtime execution exception")
                    if (e.cause is ResolvableApiException)
                    (e.cause as ResolvableApiException).startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_STATE
                    )
                }
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Timber.i("My filters: callback ${locationResult.lastLocation}")
                    getCurrentLocation()
                    mFusedLocationClient!!.removeLocationUpdates(this)
                }
            }

            mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Timber.i("My filters: last location: $location")
                    updateLocation(location)
                } else {
                    Timber.i("My filters: Location was null")
                    mFusedLocationClient!!.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
            }
        } else {
            // A local method to request required permissions;
            getLocationPermission()
        }
    }

    open fun updateLocation(location: Location) {
        Timber.i("My filters: Updating Location from base $location")
    }

    private fun getLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val dialogView = layoutInflater.inflate(R.layout.location_permission_dialog, null)
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("ALLOW") { dialog, which ->
                    requestPermissions(
                        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                        Companion.LOCATION_PERMISSION_CODE
                    )
                }
                .setNegativeButton("CANCEL") { dialog, id ->
                    dialog.dismiss()
                }.create().show()
        } else {
            requestPermissions(
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                Companion.LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Companion.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                    getCurrentLocation()
                }
            }
        }
    }

    companion object {
        // variable for location permission
        private val LOCATION_PERMISSION_CODE = 1
    }
}
