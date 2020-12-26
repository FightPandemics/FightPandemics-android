package com.fightpandemics.profile.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.profile.*
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
import com.fightpandemics.profile.domain.UpdateCurrentUserUseCase
import com.fightpandemics.profile.domain.UpdateIndividualAccountUseCase
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class ProfileViewModel @Inject constructor(
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val updateIndividualAccountUseCase: UpdateIndividualAccountUseCase
) : ViewModel() {
    val individualProfile = MutableLiveData<IndividualProfileViewState>()

    lateinit var currentProfile: IndividualProfileResponse
    data class IndividualProfileViewState(
        var isLoading: Boolean,
        val firstName: String? = null,
        val lastName: String? = null,
        val imgUrl: String? = null,
        val location: String? = null,
        val bio: String? = null,
        val facebook: String? = null,
        val instagram: String? = null,
        val linkedin: String? = null,
        val twitter: String? = null,
        val github: String? = null,
        val website: String? = null,
        val error: String?
    )

    @ExperimentalCoroutinesApi
    fun getIndividualProfile() {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            val deferredProfile = async {
                loadCurrentUserUseCase.execute("test")
            }
            deferredProfile.await().catch {

            }.collect {
                when (it) {
                    is Result.Success -> {
                        Timber.i("Debug: Result was a success, ${it.data}")
                        currentProfile = it.data as IndividualProfileResponse
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            firstName = it.data.firstName.capitalizeFirstLetter(),
                            lastName = it.data.lastName.capitalizeFirstLetter(),
                            imgUrl = "",
                            location = it.data.location.city.capitalizeFirstLetter() + " , " + it.data.location.state.capitalizeFirstLetter() + " , " + it.data.location.country.capitalizeFirstLetter(),
                            bio = it.data.about,
                            facebook = it.data.urls?.facebook,
                            instagram = it.data.urls?.instagram,
                            linkedin = it.data.urls?.linkedin,
                            twitter = it.data.urls?.twitter,
                            github = it.data.urls?.github,
                            website = it.data.urls?.website,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Result was a failure, ${it.exception.message}")
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            error = it.exception.message.toString()
                        )
                    }
                }
            }
        }
    }

    fun updateProfile(updatedProfile: PatchIndividualProfileRequest) {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            async {
                updateCurrentUserUseCase(updatedProfile)
            }.await().collect {
                when (it) {
                    is Result.Success -> {
                        getIndividualProfile()
                        Timber.i("Debug: Update was a success: ${it.data}")
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Update was a failure: ${it.exception.message}")
                    }
                }
            }
        }
    }

    fun updateAccount(updatedAccount: PatchIndividualAccountRequest) {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            async {
                updateIndividualAccountUseCase(updatedAccount)
            }.await().collect {
                when (it) {
                    is Result.Success -> {
                        getIndividualProfile()
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
