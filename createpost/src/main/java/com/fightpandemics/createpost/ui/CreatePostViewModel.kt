package com.fightpandemics.createpost.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.capitalizeFirstLetter
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.data.model.CreatePostResponse
import com.fightpandemics.createpost.domain.CreatePostsUseCase
import com.fightpandemics.createpost.domain.LoadCurrentUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class CreatePostViewModel
@ExperimentalCoroutinesApi
@Inject constructor(
    private val createPostsUseCase: CreatePostsUseCase,
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase
) : ViewModel() {

    val individualProfile = MutableLiveData<IndividualProfileViewState>()
    lateinit var currentProfile: IndividualProfileResponse
    var titleNotEmpty = MutableLiveData<Boolean>()
    var descriptionNotEmpty = MutableLiveData<Boolean>()
    var isTagSet = MutableLiveData<Boolean>()
    var allDataFilled = MutableLiveData<Boolean>()
    private val _networkResponse = MutableLiveData<Any>()
    val networkResponse: LiveData<Any> = _networkResponse

    fun setDataFilled() {
        allDataFilled.value =
            titleNotEmpty.value == true && descriptionNotEmpty.value == true && isTagSet.value == true
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun postContent(createPostRequest: CreatePostRequest) {
        viewModelScope.launch {
            val deferredCreatePost = async {
                createPostsUseCase(createPostRequest)
            }
            deferredCreatePost.await().catch {
            }.collect {
                when (it) {
                    is Result.Success -> {
                        val createPostResponse = it.data as CreatePostResponse
                        _networkResponse.value = createPostResponse
                    }
                    is Result.Error -> {
                        val createPostError = it.exception
                        _networkResponse.value = createPostError
                    }
                }
            }
        }
    }

    data class IndividualProfileViewState(
        var isLoading: Boolean,
        val firstName: String? = null,
        val lastName: String? = null,
        val imgUrl: String? = null,
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
                        currentProfile = it.data
                        individualProfile.value = IndividualProfileViewState(
                            isLoading = false,
                            firstName = it.data.firstName.capitalizeFirstLetter(),
                            lastName = it.data.lastName.capitalizeFirstLetter(),
                            imgUrl = it.data.photo,
                            error = null,
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
}
