package com.fightpandemics.filter.ui

import com.fightpandemics.home.BuildConfig
import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Pair
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
//class FilterViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
class FilterViewModel @Inject constructor() : ViewModel() {

    // Data class for making a filter request
    data class filterRequest(
        val location: String,
        val latLgn: Pair<Float, Float>,
        val fromWhomFilters: List<String>,
        val typeFilters: List<String>
    )

    // Handle visibility properties
    var isLocationOptionsExpanded = MutableLiveData<Boolean>()
    var isFromWhomOptionsExpanded = MutableLiveData<Boolean>()
    var isTypeOptionsExpanded = MutableLiveData<Boolean>()

    // Recycler View variables
    var locationQuery = MutableLiveData<String>()
    var autocomplete_locations = MutableLiveData<List<String>>()

    // handle on selected place event (either from recycler view or from current location button)
    var onSelectedLocation = MutableLiveData<String>()

    // Values that will go into home module (besides locationQuery)
    var fromWhomFilters = MutableLiveData<List<String>>()
    var fromWhomCount = MutableLiveData<Int>()
    var typeFilters = MutableLiveData<List<String>>()
    var typeCount = MutableLiveData<Int>()


    init {
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
        locationQuery.value = ""
        onSelectedLocation.value = null
        fromWhomCount.value = 0
        typeCount.value = 0
    }

    fun clearLiveDataFilters(){
        locationQuery.value = ""
        fromWhomCount.value = 0
        typeCount.value = 0
        fromWhomFilters.value = null
        typeFilters.value = null
        Timber.i("Clear: Cleared my data")
    }

    fun toggleView(optionsCardState: MutableLiveData<Boolean>) {
        optionsCardState.value = !optionsCardState.value!!
    }

    fun getFiltersAppliedCount(): Int {
        val fromWhomCount = fromWhomFilters.value?.size ?: 0
        val typeCount = typeFilters.value?.size ?: 0
        var total = fromWhomCount + typeCount
        if (locationQuery.value?.isNotBlank() == true){
            total += 1
        }
        return total
    }

    fun requestCurrentLocation(placesClient: PlacesClient) {

        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                onSelectedLocation.value = response.placeLikelihoods[0].place.address ?: "Not found"
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.statusCode}")
                }
            }
        }

    }

    fun autocompleteLocation(query: String, placesClient: PlacesClient) {

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        // Create a RectangularBounds object.
//        val bounds = RectangularBounds.newInstance(
//            LatLng(-33.880490, 151.184363),
//            LatLng(-33.858754, 151.229596)
//        )

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
//                .setOrigin(LatLng(-33.8749937, 151.2041382))
//                .setCountries("AU", "NZ")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()

        val placesList: MutableList<String> = mutableListOf()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                Timber.i("Places: success")
                for (prediction in response.autocompletePredictions) {
                    placesList.add(prediction.getPrimaryText(null).toString())
//                    Timber.i(prediction.placeId)
//                    Timber.i(prediction.getPrimaryText(null).toString())
                    // todo do something with the latlng
                    getLatLng(prediction.placeId, placesClient)
                }
                // update the live data
                autocomplete_locations.value = placesList

            }.addOnFailureListener { exception: Exception? ->
                Timber.i("Places: failure")
                if (exception is ApiException) {
                    Timber.e("Place not found: " + exception.statusCode)
                }
            }

    }

    private fun getLatLng(placeId: String, placesClient: PlacesClient) {
        // Specify the fields to return.
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                Timber.i("Places found: ${place.name}, ${place.latLng}")
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Timber.i("Places not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }

}