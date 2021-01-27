package com.fightpandemics.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.result.Result
import com.fightpandemics.home.domain.LikePostUsecase
import com.fightpandemics.home.domain.LoadPostUseCase
import com.fightpandemics.home.domain.ObserveUserAuthStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class PostDetailsViewModel @Inject constructor(
    private val loadPostUseCase: LoadPostUseCase,
    private val likePostUseCase: LikePostUsecase,
    private val observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
) : ViewModel(), HomeEventListener {

    private val _isSignedIn = MutableLiveData<Boolean>()
    private val _userId = MutableLiveData<String?>(null)

    private val _postDetailState: MutableLiveData<PostDetailState> = MutableLiveData()

    val postDetailState: LiveData<PostDetailState>
        get() = _postDetailState

    init {
        _postDetailState.value = PostDetailState(isLoading = true, post = null, errorMessage = null)

        viewModelScope.launch {
            when (val result = observeUserAuthStateUseCase(Any())) {
                is Result.Success -> {
                    _isSignedIn.value = result.data.signedIn
                    _userId.value = result.data.userId
                }
            }
        }
    }

    fun getPost(postId: String) {
        _postDetailState.value = _postDetailState.value?.copy(isLoading = true)

        viewModelScope.launch {
            async {
                loadPostUseCase(postId)
            }.await().collect { result ->
                when (result) {
                    is Result.Success -> _postDetailState.value = _postDetailState.value?.copy(
                        isLoading = false,
                        post = result.data
                    )
                    is Result.Error -> {
                        _postDetailState.value = _postDetailState.value?.copy(
                            isLoading = false,
                            post = null,
                            errorMessage = "Oops!"
                        )
                        Timber.e("Error getting post $postId ${result.exception}")
                    }
                }
            }
        }
    }

    private fun getPostComments() {

    }

    override fun userId(): String? {
        return _userId.value
    }

    override fun onLikeClicked(post: Post) {
        // if user is not signed in
        if (!_isSignedIn.value!!) {
            Timber.e("Showing Profile Sigin after LikeClicked")
            // _navigateToSignInDialogAction.value = Event(Unit)
            return
        }

        // if user is signed in
        viewModelScope.launch {
            val data = likePostUseCase(post)
        }
    }

    override fun onEditClicked(post: Post) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClicked(post: Post) {
        TODO("Not yet implemented")
    }
}


data class PostDetailState(
    val isLoading: Boolean,
    val post: Post?,
    val errorMessage: String?
)