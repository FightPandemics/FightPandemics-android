package com.fightpandemics.profile.ui

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
class ProfileViewModel @Inject constructor(
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
) : ViewModel(){
    val individualProfile = MutableLiveData<IndividualProfileViewState>()
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
    fun getndividualProfile() {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            val deferredProfile = async {
                loadCurrentUserUseCase.execute("test")
            }
            deferredProfile.await().catch {

            }.collect {
                when (it) {
                    is Result.Success -> {
                        val individualProfileResponse = it.data as IndividualProfileResponse
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            fullName = individualProfileResponse.firstName.capitalizeFirstLetter() + " " + individualProfileResponse.lastName.capitalizeFirstLetter(),
                            imgUrl = "",
                            location = individualProfileResponse.location.city.capitalizeFirstLetter() + " , " + individualProfileResponse.location.state.capitalizeFirstLetter() + " , " + individualProfileResponse.location.country.capitalizeFirstLetter(),
                            bio = "",
                            facebook = individualProfileResponse.urls?.facebook,
                            instagram = individualProfileResponse.urls?.instagram,
                            linkedin = individualProfileResponse.urls?.linkedin,
                            twitter = individualProfileResponse.urls?.twitter,
                            github = individualProfileResponse.urls?.github,
                            website = individualProfileResponse.urls?.website,
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
