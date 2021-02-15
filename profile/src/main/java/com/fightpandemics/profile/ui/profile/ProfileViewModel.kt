package com.fightpandemics.profile.ui.profile

import android.location.Location
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
import com.fightpandemics.core.data.model.userlocation.LocationRequest
import com.fightpandemics.core.result.Result
import com.fightpandemics.profile.domain.*
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
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
    private val updateIndividualAccountUseCase: UpdateIndividualAccountUseCase,
    private val userLocationUseCase: UserLocationUseCase,
    private val locationPredictionsUseCase: LocationPredictionsUseCase
) : ViewModel() {
    private val _currentLocationState = MutableStateFlow(UserLocationViewState(isLoading = true))
    private val _searchLocationState = MutableStateFlow(mutableListOf(""))
    private var sharelocationJob: Job? = null
    private var searchlocationJob: Job? = null
    private val _postsState = MutableLiveData<PostsViewState>()
    val individualProfile = MutableLiveData<IndividualProfileViewState>()
    val currentLocationState = _currentLocationState.asStateFlow()
    val postsState: LiveData<PostsViewState> get() = _postsState
    var locationQuery = MutableLiveData<String?>("")
    val searchQuery = MutableStateFlow("")
    val searchLocationState = _searchLocationState.asStateFlow()

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
    init {
        searchLocation()
    }
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

    // Get user location from API using lat & lng
    fun updateCurrentLocation(location: Location) {
        sharelocationJob?.cancel()
        sharelocationJob = viewModelScope.launch {
            userLocationUseCase(LocationRequest(location.latitude, location.longitude))
                .collect {
                    when (it) {
                        is Result.Loading -> currentLocation(true, null, null)
                        is Result.Success -> currentLocation(false, null, it.data as com.fightpandemics.core.data.model.userlocation.Location)
                        is Result.Error -> currentLocation(true, it, null)
                    }
                }
        }
    }
    // Search for location from API using user input
    fun searchLocation() {
//        _searchLocationState.value = mutableListOf("Feriel", "fp", "liheouil", "test")
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
        currentLocation: com.fightpandemics.core.data.model.userlocation.Location?
    ) {
        _currentLocationState.value =
            UserLocationViewState(loading, error, "${currentLocation?.address}")
    }

    data class UserLocationViewState(
        var isLoading: Boolean,
        val error: Result.Error? = null,
        val userLocation: String? = null
    )
}
