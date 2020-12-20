package com.fightpandemics.profile.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.profile.*
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
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
) : ViewModel(){
    val individualProfile = MutableLiveData<IndividualProfileViewState>()
    lateinit var currentProfile: IndividualProfileResponse
    data class IndividualProfileViewState(
        var isLoading: Boolean,
        val fullName: String? = null,
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
                        currentProfile = it.data as IndividualProfileResponse
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            fullName = currentProfile.firstName.capitalizeFirstLetter() + " " + currentProfile.lastName.capitalizeFirstLetter(),
                            imgUrl = "",
                            location = currentProfile.location.city.capitalizeFirstLetter() + " , " + currentProfile.location.state.capitalizeFirstLetter() + " , " + currentProfile.location.country.capitalizeFirstLetter(),
                            bio = currentProfile.about,
                            facebook = currentProfile.urls?.facebook,
                            instagram = currentProfile.urls?.instagram,
                            linkedin = currentProfile.urls?.linkedin,
                            twitter = currentProfile.urls?.twitter,
                            github = currentProfile.urls?.github,
                            website = currentProfile.urls?.website,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            error = it.exception.message.toString()
                        )
                    }
                }
            }
        }
    }




}
