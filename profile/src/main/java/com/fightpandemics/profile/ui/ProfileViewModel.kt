package com.fightpandemics.profile.ui

import DataClasses.User
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.result.Result
import com.fightpandemics.login.ui.LoginViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ActivityScope
class ProfileViewModel @Inject constructor() : ViewModel(){
    private val _individualProfile = MutableLiveData<IndividualProfileViewState>()
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
        _individualProfile.value?.isLoading = true
        viewModelScope.launch {
            val deferredProfile = async {
                loginUseCase()
            }
            deferredProfile.await().catch {

            }.collect {
                when (it) {
                    is Result.Success -> {
                        val individualProfileResponse = it.data as IndividualProfileResponse
                        _individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            fullName = individualProfileResponse.firstName + individualProfileResponse.lastName,
                            imgUrl = "",
                            location = individualProfileResponse.location.city + ", " + individualProfileResponse.location.country,
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
                        _individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            error = it.exception.message.toString()
                        )
                    }
                }
            }
        }
    }




}
