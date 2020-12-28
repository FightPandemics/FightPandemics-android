package com.fightpandemics.createpost.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.result.Result
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.data.model.CreatePostResponse
import com.fightpandemics.createpost.domain.CreatePostsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class CreatePostViewModel
@ExperimentalCoroutinesApi
@Inject constructor(private val createPostsUseCase: CreatePostsUseCase) : ViewModel() {

    var titleNotEmpty = MutableLiveData<Boolean>()
    var descriptionNotEmpty = MutableLiveData<Boolean>()
    var isTagSet = MutableLiveData<Boolean>()
    var allDataFilled = MutableLiveData<Boolean>()
    private val _networkResponse = MutableLiveData<Any>()
    val networkResponse: LiveData<Any> = _networkResponse
    
    fun setDataFilled() {
        allDataFilled.value = titleNotEmpty.value == true && descriptionNotEmpty.value == true && isTagSet.value == true
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
}