package com.fightpandemics.profile.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.UpdateCurrentUserUseCase
import com.fightpandemics.profile.domain.UpdateIndividualAccountUseCase
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class EditProfileViewModel @Inject constructor(
//    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val updateIndividualAccountUseCase: UpdateIndividualAccountUseCase
) : ViewModel(){

    lateinit var currentProfile: IndividualProfileResponse

    private val _updateState = MutableLiveData<ProfileViewModel.IndividualProfileViewState>()
    val updateState: LiveData<ProfileViewModel.IndividualProfileViewState> get() = _updateState

    fun getName(): String =
        currentProfile?.firstName?.capitalizeFirstLetter()  + " " + currentProfile?.lastName?.capitalizeFirstLetter()

    fun updateProfile(updatedProfile: PatchIndividualProfileRequest){
        _updateState.value?.isLoading = true
        viewModelScope.launch {
            async {
                updateCurrentUserUseCase(updatedProfile)
            }.await().collect {
                when(it){
                    is Result.Success -> {
                        Timber.i("Debug: Update was a success: ${it.data}")
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Update was a failure: ${it.exception.message}")
                    }
                }
            }
        }
    }

    fun updateAccount(updatedAccount: PatchIndividualAccountRequest){
        _updateState.value?.isLoading = true
        viewModelScope.launch {
            async {
                updateIndividualAccountUseCase(updatedAccount)
            }.await().collect {
                when(it){
                    is Result.Success -> {
                        Timber.i("Debug: Update was a success: ${it.data}")
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Update was a failure: ${it.exception.message}")
                    }
                }
            }
        }
    }

}
