package com.fightpandemics.login.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.CompleteProfileResponse
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.model.login.SignUpResponse
import com.fightpandemics.core.data.model.login.User
import com.fightpandemics.core.data.model.userlocation.LocationRequest
import com.fightpandemics.core.data.model.userlocationpredictions.Prediction
import com.fightpandemics.core.result.Result
import com.fightpandemics.login.domain.CompleteProfileUseCase
import com.fightpandemics.login.domain.LocationDetailsUseCase
import com.fightpandemics.login.domain.LocationPredictionsNameAndIdUseCase
import com.fightpandemics.login.domain.LoginUseCase
import com.fightpandemics.login.domain.SignUPUseCase
import com.fightpandemics.login.domain.UserLocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@ActivityScope
class LoginViewModel @Inject constructor(
    private val locationDetailsUseCase: LocationDetailsUseCase,
    private val locationPredictionsNameAndIdUseCase: LocationPredictionsNameAndIdUseCase,
    private val userLocationUseCase: UserLocationUseCase,
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUPUseCase,
    private val completeProfileUseCase: CompleteProfileUseCase
) : ViewModel() {
    // location variables
    private var sharelocationJob: Job? = null
    private var searchlocationJob: Job? = null
    val searchQuery = MutableStateFlow("")

    lateinit var completeProfileLocation: com.fightpandemics.core.data.model.login.Location

    private val _currentLocationState = MutableStateFlow(UserLocationViewState(isLoading = true))
    val currentLocationState = _currentLocationState.asStateFlow()
    private val _searchLocationState = MutableStateFlow(mutableListOf<Prediction>())
    val searchLocationState = _searchLocationState.asStateFlow()

    // variable to keep track of selected location, also allows us to delete not-selected input from user
    private val _locationSelected = MutableLiveData("")
    val locationSelected = _locationSelected

    private val _login = MutableLiveData<LoginViewState>()
    private val _signup = MutableLiveData<SignUPViewState>()
    private val _completeProfile = MutableLiveData<CompleteProfileViewState>()
    val login: LiveData<LoginViewState> = _login
    val signup: LiveData<SignUPViewState> = _signup
    val completeProfile: LiveData<CompleteProfileViewState> = _completeProfile

    init {
        searchLocation()
    }

    fun doLogin(email: String, password: String) {
        _login.value?.isLoading = true
        viewModelScope.launch {
            val deferredLogin = async {
                loginUseCase(LoginRequest(email, password))
            }
            deferredLogin.await().catch {
            }.collect {
                when (it) {
                    is Result.Success -> {
                        val loginResponse = it.data as LoginResponse
                        _login.value = LoginViewState(
                            false,
                            loginResponse.email,
                            loginResponse.emailVerified!!,
                            loginResponse.token,
                            loginResponse.user,
                            null
                        )
                    }
                    is Result.Error -> {
                        _login.value = LoginViewState(
                            false,
                            null,
                            false,
                            null,
                            null,
                            it.exception.message.toString()
                        )
                    }
                }
            }
        }
    }

    fun doSignUP(email: String, password: String, confirmPassword: String) {
        signup.value?.isLoading = true

        viewModelScope.launch {
            val request = SignUpRequest(email, password, confirmPassword) // PostRequest
            val couroutineResponse = async {
                signUpUseCase.invoke(request)
            }
            couroutineResponse.await().collect {
                when (it) {
                    is Result.Success -> {
                        val signUpResponse = it.data as SignUpResponse
                        _signup.value = SignUPViewState(
                            false,
                            signUpResponse.emailVerified,
                            signUpResponse.token,
                            null,
                            isError = false
                        )
                    }
                    is Result.Error -> {
                        _signup.value = SignUPViewState(
                            isLoading = false,
                            emailVerified = false,
                            token = null,
                            error = it.exception.message.toString(),
                            isError = true
                        )
                    }
                }
            }
        }
    }

    fun doCompleteProfile(request: CompleteProfileRequest) {
        completeProfile.value?.isLoading = true

        viewModelScope.launch {
            val couroutineResponse = async {
                completeProfileUseCase.invoke(request)
            }
            couroutineResponse.await().collect {
                when (it) {
                    is Result.Success -> {
                        val completeResponse = it.data as CompleteProfileResponse
                        _completeProfile.value = CompleteProfileViewState(
                            false,
                            completeResponse.email,
                            null,
                            error = null,
                            isError = false
                        )
                    }
                    is Result.Error -> {
                        _completeProfile.value = CompleteProfileViewState(
                            isLoading = false,
                            email = "",
                            token = null,
                            error = it.exception.message.toString(),
                            isError = true
                        )
                    }
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
                        is Result.Success -> currentLocation(
                            false,
                            null,
                            it.data as com.fightpandemics.core.data.model.userlocation.Location
                        )
                        is Result.Error -> currentLocation(true, it, null)
                    }
                }
        }
    }

//     Search for location from API using user input
    @FlowPreview
    fun searchLocation() {
        searchlocationJob?.cancel()
        searchlocationJob = viewModelScope.launch {
            searchQuery
                .debounce(timeoutMillis = 300)
                .filter { return@filter it.isNotEmpty() && it.length >= LEN_FOR_SUGGESTIONS }
                .distinctUntilChanged()
                .map { it.trim() }
                .flatMapLatest { locationPredictionsNameAndIdUseCase(it) }
                .conflate()
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _searchLocationState.value =
                                it.data as MutableList<Prediction>
                        }
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

        if (currentLocation != null) {
            completeProfileLocation = com.fightpandemics.core.data.model.login.Location(
                currentLocation.address ?: "",
                currentLocation.city ?: "",
                listOf(
                    currentLocation.coordinates?.get(0) ?: 0.0,
                    currentLocation.coordinates?.get(1) ?: 0.0
                ),
                currentLocation.country ?: "",
                currentLocation.state ?: ""
            )
        }
        _currentLocationState.value =
            UserLocationViewState(loading, error, "${currentLocation?.address}")
    }

    fun getLocationDetails(placeId: String?) {
        Timber.i("Debug: my placeId is $placeId")
        viewModelScope.launch {
            locationDetailsUseCase.invoke(placeId).collect {
                when (it) {
                    is Result.Success -> {
                        val locationDetails = it.data as com.fightpandemics.core.data.model.userlocationdetails.Location
                        Timber.i("Debug: my location prediction details $locationDetails")
                        completeProfileLocation =
                            com.fightpandemics.core.data.model.login.Location(
                                locationDetails.address ?: "",
                                locationDetails.city ?: "",
                                locationDetails.coordinates ?: listOf(),
                                locationDetails.country ?: "",
                                locationDetails.state ?: ""
                            )
                    }
                    is Result.Loading -> Timber.i("Debug: getDetails is loading}")
                    is Result.Error -> Timber.i("Debug: there was an error with getDetails, ${it.exception.message}")
                }
            }
        }
    }

    fun resetOldLocationSelected() {
        _locationSelected.value = ""
        completeProfileLocation = com.fightpandemics.core.data.model.login.Location(
            "",
            "",
            listOf(0.0, 0.0),
            "",
            ""
        )
    }

    companion object {
        const val LEN_FOR_SUGGESTIONS = 3
    }
}

/**
 * UI Models for [ SignInEmailFragment ].
 */
data class LoginViewState(
    var isLoading: Boolean,
    val email: String?,
    val emailVerified: Boolean,
    val token: String?,
    val user: User?,
    val error: String?
)

data class SignUPViewState(
    var isLoading: Boolean,
    val emailVerified: Boolean,
    val token: String?,
    val error: String?,
    val isError: Boolean
)
data class CompleteProfileViewState(
    var isLoading: Boolean,
    val email: String,
    val token: String?,
    val error: String?,
    val isError: Boolean
)
data class UserLocationViewState(
    var isLoading: Boolean,
    val error: Result.Error? = null,
    val userLocation: String? = null,
)

/*
*
* class LoginViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _signUp = MutableLiveData<Result<SignUpModel>>()
    val signUp: LiveData<Result<SignUpModel>> = _signUp

    private val _changePassword = MutableLiveData<Result<ChangePasswordModel>>()
    val changePassword: LiveData<Result<ChangePasswordModel>> = _changePassword

    fun executeSignUp(email: String, password: String, confirmPassword: String) {
        val signUpRequest = SignUpRequest(email, password, confirmPassword)
        _signUp.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val response = repository.signUp(signUpRequest)
                if (response.isSuccessful && response.code() == 0) {
                    val model = SignUpDataMapper().transformRemoteToLocal(response.body()!!)
                    _signUp.postValue(Result.Success(model))
                    //Consume the response here - Save it to local or display to the User if needed
                } else {
                    _signUp.postValue(Result.Error(Exception(response.message())))
                }
            } catch (error: Throwable) {
                _signUp.postValue(Result.Error(error))
            }
        }
    }

    fun changePassword(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.changePassword(email)
                if (response.isSuccessful && response.code() == 0) {
                    val model = ChangePasswordDataMapper().transformRemoteToLocal(response.body()!!)
                    _changePassword.postValue(Result.Success(model))
                    //Consume the response here - Save it to local or display to the User if needed
                } else {
                    _changePassword.postValue(Result.Error(Exception(response.message())))
                }
            } catch (error: Throwable) {
                _changePassword.postValue(Result.Error(error))
            }
        }
    }
}
* */
