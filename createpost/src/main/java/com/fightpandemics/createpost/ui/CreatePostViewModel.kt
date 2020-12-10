package com.fightpandemics.createpost.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class CreatePostViewModel @Inject constructor() : ViewModel() {

    var titleNotEmpty = MutableLiveData<Boolean>()
    var descriptionNotEmpty = MutableLiveData<Boolean>()
    var isTagSet = MutableLiveData<Boolean>()
    var allDataFilled = MutableLiveData<Boolean>()

    init {

    }
    
    fun setDataFilled() {
        allDataFilled.value = titleNotEmpty.value == true && descriptionNotEmpty.value == true && isTagSet.value == true
    }
}