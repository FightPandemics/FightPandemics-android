package com.fightpandemics.filter.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class FilterViewModel @Inject constructor() : ViewModel() {
    /*
    * - should the list of chips be updated everytime a new chip is picked?
    * - variable:
    * isExpanded - show options menu
    * chipSelected - total chips
    * fromwhomchips
    * typechips
    * location
    * */

    var isLocationOptionsExpanded = MutableLiveData<Boolean>()
    var isFromWhomOptionsExpanded = MutableLiveData<Boolean>()
    var isTypeOptionsExpanded = MutableLiveData<Boolean>()

    var locationQuery = MutableLiveData<CharSequence>()
    var random_locations = MutableLiveData<List<String>>()
    var autocomplete_locations = MutableLiveData<List<String>>()

    init {
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
        random_locations.value = listOf("California, USA", "Massachusetts, USA", "Panama, Panama", "New York, USA")
    }

    fun toggleView(optionsCardState: MutableLiveData<Boolean>) {
        optionsCardState.value = !optionsCardState.value!!
    }

//    private fun getCurrentLocation() {
//        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)
//        // Create a new PlacesClient instance
//        val placesClient = Places.createClient(requireContext())
//
//        // Use fields to define the data types to return.
//        val placeFields: List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
//
//        // Use the builder to create a FindCurrentPlaceRequest.
//        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
//
//        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//
////            Toast.makeText(requireContext(), "You already have permissions, great!", Toast.LENGTH_SHORT).show()
//
//            val placeResponse = placesClient.findCurrentPlace(request)
//            placeResponse.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val response = task.result
//                    binding.locationOptions.locationSearch.setText(
//                        response.placeLikelihoods[0].place.address ?: "Not found"
//                    )
////                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
////                        ?: emptyList()) {
////                        binding.locationOptions.locationSearch.setText(placeLikelihood.place.address)
////                        break
////                        Timber.i("Place '${placeLikelihood.place.address}', '${placeLikelihood.place.latLng}' has likelihood: ${placeLikelihood.likelihood}")
////                        Toast.makeText(requireContext(), "Place '${placeLikelihood.place.address}', '${placeLikelihood.place.latLng}' has likelihood: ${placeLikelihood.likelihood}", Toast.LENGTH_SHORT).show()
////                    }
//                } else {
//                    val exception = task.exception
//                    if (exception is ApiException) {
//                        Timber.e("Place not found: ${exception.statusCode}")
//                        Toast.makeText(
//                            requireContext(),
//                            "Place not found: Exception status code ${exception.statusCode}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        } else {
//            // A local method to request required permissions;
//            // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission()
//        }
//    }
//
//    private fun getLocationPermission() {
//        // TODO: Add custom alert dialog
//        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//            AlertDialog.Builder(requireContext())
//                .setTitle("Allow FightPandemics to access your location?")
//                .setMessage("FightPandemics uses location to show hospitals and aid information near you.")
//                .setPositiveButton("ALLOW", DialogInterface.OnClickListener { dialog, which ->
//                    requestPermissions(
//                        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
//                        STORAGE_PERMISSION_CODE
//                    )
//                })
//                .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->
//                    dialog.dismiss()
//                }).create().show()
//        } else {
//            requestPermissions(
//                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
//                STORAGE_PERMISSION_CODE
//            )
//        }
//
//    }


}