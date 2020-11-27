package com.fightpandemics.filter.ui

import androidx.lifecycle.*
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.model.userlocation.LocationRequest
import com.fightpandemics.core.result.Result
import com.fightpandemics.filter.domain.LocationDetailsUseCase
import com.fightpandemics.filter.domain.LocationPredictionsUseCase
import com.fightpandemics.filter.domain.UserLocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import android.location.Location as Location1
import com.fightpandemics.core.data.model.userlocation.Location as Location2

/*
* created by Osaigbovo Odiase & Jose Li
* */
@ExperimentalCoroutinesApi
@FlowPreview
@ActivityScope
class FilterViewModel @Inject constructor(
    private val userLocationUseCase: UserLocationUseCase,
    private val locationPredictionsUseCase: LocationPredictionsUseCase,
    /*private val locationDetailsUseCase: LocationDetailsUseCase*/
) : ViewModel() {

    private var sharelocationJob: Job? = null
    private var searchlocationJob: Job? = null

    private val _currentLocationState = MutableStateFlow(UserLocationViewState(isLoading = true))
    val currentLocationState = _currentLocationState.asStateFlow()

    private val _searchLocationState = MutableStateFlow(mutableListOf(""))
    val searchLocationState = _searchLocationState.asStateFlow()

    val searchQuery = MutableStateFlow("")

    // Handle visibility properties
    var isLocationOptionsExpanded = MutableLiveData<Boolean>()
    var isFromWhomOptionsExpanded = MutableLiveData<Boolean>()
    var isTypeOptionsExpanded = MutableLiveData<Boolean>()

    // Values that will go into home module
    var locationQuery = MutableLiveData<String?>("")
    var fromWhomFilters = MutableLiveData<List<String?>>()
    var typeFilters = MutableLiveData<List<String?>>()

    // Keep track of selected chip counts - Used for checking when to enable apply filter button
    var fromWhomCount = MutableLiveData<Int>()
    var typeCount = MutableLiveData<Int>()

    init {
        // cards should not be expanded at start
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
        // initialize data that will be sent as a FilterRequest
        //locationQuery.value = ""
        fromWhomFilters.value = listOf()
        typeFilters.value = listOf()
        // initialize helper data
        fromWhomCount.value = 0
        typeCount.value = 0

        searchLocation()
    }

    fun clearLiveDataFilters() {
        locationQuery.value = ""
        fromWhomCount.value = 0
        typeCount.value = 0
        fromWhomFilters.value = null
        typeFilters.value = null
    }

    fun toggleView(optionsCardState: MutableLiveData<Boolean>) {
        optionsCardState.value = !optionsCardState.value!!
    }

    fun closeAllOptionCards() {
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
    }

    fun getFiltersAppliedCount(): Int {
        val fromWhomCount = fromWhomFilters.value?.size ?: 0
        val typeCount = typeFilters.value?.size ?: 0
        var total = fromWhomCount + typeCount
        if (locationQuery.value?.isNotBlank() == true) {
            total += 1
        }
        return total
    }

    // Get user location from API using lat & lng
    fun updateCurrentLocation(location: Location1) {
        sharelocationJob?.cancel()
        sharelocationJob = viewModelScope.launch {
            userLocationUseCase(LocationRequest(location.latitude, location.longitude))
                .collect {
                    when (it) {
                        is Result.Loading -> currentLocation(true, null, null)
                        is Result.Success -> currentLocation(false, null, it.data as Location2)
                        is Result.Error -> currentLocation(true, it, null)
                    }
                }
        }
    }

    // Search for location from API using user input
    fun searchLocation() {
        searchlocationJob?.cancel()
        searchlocationJob = viewModelScope.launch {
            searchQuery
                .debounce(300)
                .filter { return@filter !it.isEmpty() && it.length >= 3 }
                .distinctUntilChanged()
                .map { it.trim() }
                .flatMapLatest { locationPredictionsUseCase(it) }
                .conflate()
                .collect {
                    when (it) {
                        is Result.Success ->
                            _searchLocationState.value =
                                it.data as MutableList<String>
                        is Result.Error -> Timber.e(it.toString())
                        is Result.Loading -> Timber.e("LOADING...")
                    }
                }
        }
    }

    private fun currentLocation(
        loading: Boolean,
        error: Result.Error?,
        currentLocation: Location2?
    ) {
        _currentLocationState.value =
            UserLocationViewState(loading, error, "${currentLocation?.address}")
    }

    /*fun createFilterRequest(): FilterRequest {
        return FilterRequest(
            locationQuery.value,
            fromWhomFilters.value,
            typeFilters.value
        )
    }*/

    fun createFilterRequest(): FilterRequest {
        val stringList = mutableListOf<String?>()

        if (locationQuery.value!!.isNotBlank()) stringList.add(locationQuery.value)
        stringList.addAll(fromWhomFilters.value!!)
        stringList.addAll(typeFilters.value!!)

        return FilterRequest(
            stringList
            //locationQuery.value,
            //fromWhomFilters.value,
            //typeFilters.value
        )
    }
}

data class UserLocationViewState(
    var isLoading: Boolean,
    val error: Result.Error? = null,
    val userLocation: String? = null,
)
