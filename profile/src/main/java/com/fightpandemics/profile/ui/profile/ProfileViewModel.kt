package com.fightpandemics.profile.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.Needs
import com.fightpandemics.core.data.model.profile.Objectives
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
import com.fightpandemics.profile.domain.LoadIndividualUserPostsUseCase
import com.fightpandemics.profile.domain.UpdateCurrentUserUseCase
import com.fightpandemics.profile.domain.UpdateIndividualAccountUseCase
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class ProfileViewModel @Inject constructor(
    private val loadIndividualUserPostsUseCase: LoadIndividualUserPostsUseCase,
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val updateIndividualAccountUseCase: UpdateIndividualAccountUseCase
) : ViewModel() {
    val individualProfile = MutableLiveData<IndividualProfileViewState>()

    private val _postsState = MutableLiveData<PostsViewState>()
    val postsState: LiveData<PostsViewState> get() = _postsState

    lateinit var currentProfile: IndividualProfileResponse
    data class IndividualProfileViewState(
        var isLoading: Boolean,
        val email: String? = null,
        val id: String? = null,
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
        val error: String?,
        val objectives: Objectives? = null,
        val needs: Needs? = null
    )
    data class PostsViewState(
        var isLoading: Boolean,
        val error: Result.Error?,
        val posts: List<Post>? = null
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
                        currentProfile = it.data
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            email = it.data.email,
                            id = it.data.id,
                            firstName = it.data.firstName.capitalizeFirstLetter(),
                            lastName = it.data.lastName.capitalizeFirstLetter(),
                            imgUrl = it.data.photo,
                            location = it.data.location.city.capitalizeFirstLetter() + " , " +
                                it.data.location.state.capitalizeFirstLetter() + " , " +
                                it.data.location.country.capitalizeFirstLetter(),
                            bio = it.data.about,
                            facebook = it.data.urls.facebook,
                            instagram = it.data.urls.instagram,
                            linkedin = it.data.urls.linkedin,
                            twitter = it.data.urls.twitter,
                            github = it.data.urls.github,
                            website = it.data.urls.website,
                            error = null,
                            objectives = it.data.objectives,
                            needs = it.data.needs
                        )
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Result was a failure, ${it.exception.message}")
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            error = it.exception.message.toString()
                        )
                    }
                    else -> {
                        TODO()
                    }
                }
            }
        }
    }

    fun updateProfile(updatedProfile: PatchIndividualProfileRequest) {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                updateCurrentUserUseCase(updatedProfile)
            }.collect {
                when (it) {
                    is Result.Success -> {
                        getIndividualProfile()
                        Timber.i("Debug: Update was a success: ${it.data}")
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Update was a failure: ${it.exception.message}")
                    }
                    else -> {
                        TODO()
                    }
                }
            }
        }
    }

    fun updateAccount(updatedAccount: PatchIndividualAccountRequest) {
        individualProfile.value?.isLoading = true
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                updateIndividualAccountUseCase(updatedAccount)
            }.collect {
                when (it) {
                    is Result.Success -> {
                        getIndividualProfile()
                        Timber.i("Debug: Update was a success: ${it.data}")
                    }
                    is Result.Error -> {
                        Timber.i("Debug: Update was a failure: ${it.exception.message}")
                    }
                    else -> TODO()
                }
            }
        }
    }

    fun loadUserPosts(authorId: String) {
        // Set a default loading state
        _postsState.value?.isLoading = true

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                loadIndividualUserPostsUseCase(authorId)
            }.collect {
                when (it) {
                    is Result.Success ->
                        _postsState.value =
                            PostsViewState(
                                isLoading = false,
                                error = null,
                                posts = it.data as List<Post>?
                            )
                    is Result.Error ->
                        _postsState.value =
                            PostsViewState(isLoading = false, error = it, posts = emptyList())
                    else -> TODO()
                }
            }
        }
    }
}
