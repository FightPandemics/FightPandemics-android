package com.fightpandemics.filter.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fightpandemics.home.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.RuntimeExecutionException
import timber.log.Timber

/*
* created by Osaigbovo Odiase & Jose Li
* */
open class BaseLocationFragment: Fragment() {

    // Places API variables
    private val LOCATION_PERMISSION_CODE = 1

    //    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a FusedLocation Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        //mFusedLocationClient = null
        super.onDestroyView()
    }

    private fun getCurrentLocation() {
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            Timber.i("My filters: Starting fetching for location")

            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) // 10 seconds
                .setFastestInterval(5*1000) // 5 seconds

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

            val locationCallback = object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    Timber.i("My filters: callback ${locationResult.lastLocation}")
                    getCurrentLocation()
//                    filterViewModel.updateCurrentLocation(locationResult.lastLocation)
                }
            }

            mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    Timber.i("My filters: last location: $location")
                    //filterViewModel.updateCurrentLocation(location)
                } else{
                    Timber.i("My filters: Location was null")
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                    mFusedLocationClient.removeLocationUpdates(locationCallback)
                }
            }

        } else {
            // A local method to request required permissions;
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val dialogView = layoutInflater.inflate(R.layout.location_permission_dialog, null)
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("ALLOW") { dialog, which ->
                    requestPermissions(
                        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                        LOCATION_PERMISSION_CODE
                    )
                }
                .setNegativeButton("CANCEL") { dialog, id ->
                    dialog.dismiss()
                }.create().show()
        } else {
            requestPermissions(
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                LOCATION_PERMISSION_CODE
            )
//            Toast.makeText(context, "Permissions were denied", Toast.LENGTH_LONG).show()
        }
    }
}