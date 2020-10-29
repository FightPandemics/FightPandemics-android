package com.fightpandemics.filter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
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


}