package com.fightpandemics.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.result.Result
import com.fightpandemics.home.domain.LoadPostsUseCase
import com.fightpandemics.home.domain.ObserveUserAuthStateUseCase
import com.fightpandemics.home.domain.UpdatePostUsecase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class HomeViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase,
    private val updatePostUsecase: UpdatePostUsecase,
    private val observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel(), HomeEventListener {

    private val _postsState = MutableLiveData<PostsViewState>()
    val postsState: LiveData<PostsViewState> get() = _postsState

    private val _offerState = MutableLiveData<PostsViewState>()
    val offerState: LiveData<PostsViewState> get() = _offerState

    private val _requestState = MutableLiveData<PostsViewState>()
    val requestState: LiveData<PostsViewState> get() = _requestState

    private val _isSignedIn = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            when(val result = observeUserAuthStateUseCase(Any())){
                is Result.Success -> _isSignedIn.value = result.data
            }
        }

        getPosts(null)
        getOffers("offer")
        getRequests("request")
    }

    fun getPosts(objective: String?): Job {
        // Set a default loading state
        _postsState.value?.isLoading = true
        return viewModelScope.launch {
            val deferredPosts = async {
                loadPostsUseCase(objective)
            }
            deferredPosts.await().collect {
                when (it) {
                    is Result.Success -> _postsState.value =
                        PostsViewState(isLoading = false, error = null, posts = it.data)
                    is Result.Error -> _postsState.value =
                        PostsViewState(isLoading = false, error = it, posts = emptyList())
                }
            }
        }
    }

    fun getOffers(objective: String?): Job {
        // Set a default loading state
        _offerState.value?.isLoading = true
        return viewModelScope.launch {

            val ss = async {
                loadPostsUseCase(objective)
            }

            ss.await().collect {
                when (it) {
                    is Result.Success -> {
                        _offerState.value =
                            PostsViewState(isLoading = false, error = null, posts = it.data)
                    }
                    is Result.Error -> _offerState.value =
                        PostsViewState(isLoading = false, error = it, posts = emptyList())
                }
            }
        }
    }

    fun getRequests(objective: String?): Job {
        // Set a default loading state
        _requestState.value?.isLoading = true
        return viewModelScope.launch {

            val ss = async {
                loadPostsUseCase(objective)
            }

            ss.await().collect {
                when (it) {
                    is Result.Success -> {
                        _requestState.value =
                            PostsViewState(isLoading = false, error = null, posts = it.data)
                    }
                    is Result.Error -> _requestState.value =
                        PostsViewState(isLoading = false, error = it, posts = emptyList())
                }
            }
        }
    }

    override fun onLikeClicked(post: Post) {
        Timber.e(post._id)

        if (!_isSignedIn.value!!) {
            Timber.e("Showing Profile Sigin after LikeClicked")
            //_navigateToSignInDialogAction.value = Event(Unit)
            return
        }

        viewModelScope.launch {
            val data = updatePostUsecase(post)
            getPosts(null)
        }
    }
}

/**
 * UI Model for [HomeFragment].
 */
data class PostsViewState(
    var isLoading: Boolean,
    val error: Result.Error?,
    val posts: List<Post>?,
)

interface HomeEventListener : EventActions {
}
