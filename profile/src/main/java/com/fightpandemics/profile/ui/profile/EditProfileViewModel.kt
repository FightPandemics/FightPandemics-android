package com.fightpandemics.profile.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class EditProfileViewModel @Inject constructor(
    //private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
) : ViewModel(){
    lateinit var currentProfile: IndividualProfileResponse





}
