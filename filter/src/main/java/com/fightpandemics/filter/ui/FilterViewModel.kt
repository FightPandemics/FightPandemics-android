package com.fightpandemics.filter.ui

import android.location.Location
import android.location.LocationManager
import android.util.Pair
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@ActivityScope
//class FilterViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
class FilterViewModel @Inject constructor() : ViewModel() {

    // Handle visibility properties
    var isLocationOptionsExpanded = MutableLiveData<Boolean>()
    var isFromWhomOptionsExpanded = MutableLiveData<Boolean>()
    var isTypeOptionsExpanded = MutableLiveData<Boolean>()

    // Recycler View autocomplete location variable
    var autocomplete_locations = MutableLiveData<HashMap<String, MutableList<String>>>()

    // handle on selected place event (either from recycler view or from current location button)
    var onSelectedLocation = MutableLiveData<String>()

    // Values that will go into home module
    var locationQuery = MutableLiveData<String>()
    var fromWhomFilters = MutableLiveData<List<String>>()
    var typeFilters = MutableLiveData<List<String>>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    // Keep track of selected chip counts - Used for checking when to enable apply filter button
    var fromWhomCount = MutableLiveData<Int>()
    var typeCount = MutableLiveData<Int>()

    init {
        // cards should not be expanded at start
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
        // initialize data that will be sent as a FilterRequest
        locationQuery.value = ""
        fromWhomFilters.value = listOf()
        typeFilters.value = listOf()
        latitude.value = null
        longitude.value = null
        // initialize helper data
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
    }

    fun toggleView(optionsCardState: MutableLiveData<Boolean>) {
        optionsCardState.value = !optionsCardState.value!!
    }

    fun closeOptionCards(){
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
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
                onSelectedLocation.value = response.placeLikelihoods[0].place.address
                latitude.value = response.placeLikelihoods[0].place.latLng?.latitude
                longitude.value = response.placeLikelihoods[0].place.latLng?.longitude
                Timber.i("My filters : places ${latitude.value}, ${longitude.value}")
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.statusCode}")
                }
            }
        }
    }

    fun updateCurrentLocation(location: Location){
        onSelectedLocation.value = "Todo: get place name from API"
        latitude.value = location.latitude
        longitude.value = location.longitude
        Timber.i("My filters : Location Manager View Model ${latitude.value}, ${longitude.value}")
    }

    fun autocompleteLocation(query: String, placesClient: PlacesClient) {
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()

        // initialize map
        val placesMap = HashMap<String, MutableList<String>>()
        placesMap["names"] = mutableListOf()
        placesMap["ids"] = mutableListOf()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                Timber.i("Places: success")
                for (prediction in response.autocompletePredictions) {
                    val placeName = prediction.getPrimaryText(null).toString()
                    val placeId = prediction.placeId
                    placesMap["names"]!!.add(placeName)
                    placesMap["ids"]!!.add(placeId)
                }
                // update the live data
                autocomplete_locations.value = placesMap
            }.addOnFailureListener { exception: Exception? ->
                Timber.i("Places: failure")
                if (exception is ApiException) {
                    Timber.e("Place not found: " + exception.statusCode)
                }
            }
    }

    fun getLatLng(placeId: String, placesClient: PlacesClient) {
        // Specify the fields to return.
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        // TODO: TEST OUT THAT THIS IS GETTING THE CORRECT LAT & LNG CORRECTLY
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
//                Timber.i("my filters : Places found: ${place.name}, ${place.latLng}")
//                Timber.i("my filters : Do placeId match? input: $placeId, fetched: ${place.id.toString()} : ${placeId == place.id.toString()}")
                latitude.value = place.latLng?.latitude
                longitude.value = place.latLng?.longitude
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Timber.i("Places not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }

    fun createFilterRequest (): FilterRequest{
        return FilterRequest(locationQuery.value, latitude.value, longitude.value, fromWhomFilters.value, typeFilters.value)
    }

}