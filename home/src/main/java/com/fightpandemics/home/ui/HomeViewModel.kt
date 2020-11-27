package com.fightpandemics.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.result.Event
import com.fightpandemics.core.result.Result
import com.fightpandemics.home.domain.DeletePostUsecase
import com.fightpandemics.home.domain.LikePostUsecase
import com.fightpandemics.home.domain.LoadPostsUseCase
import com.fightpandemics.home.domain.ObserveUserAuthStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
@ExperimentalCoroutinesApi
@FeatureScope
class HomeViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase,
    private val likePostUsecase: LikePostUsecase,
    private val deletePostUsecase: DeletePostUsecase,
    private val observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel(), HomeEventListener {

    val filterState = MutableStateFlow(mutableListOf(""))

    private val _postsState = MutableLiveData<PostsViewState>()
    val postsState: LiveData<PostsViewState> get() = _postsState

    private val _offerState = MutableLiveData<PostsViewState>()
    val offerState: LiveData<PostsViewState> get() = _offerState

    private val _requestState = MutableLiveData<PostsViewState>()
    val requestState: LiveData<PostsViewState> get() = _requestState

    private val _isSignedIn = MutableLiveData<Boolean>()
    private val _userId = MutableLiveData<String?>(null)

    private val _isDeleted = MutableLiveData(Event(""))
    val isDeleted: LiveData<Event<String>> get() = _isDeleted

    init {
        viewModelScope.launch {
            when (val result = observeUserAuthStateUseCase(Any())) {
                is Result.Success -> {
                    _isSignedIn.value = result.data.signedIn
                    _userId.value = result.data.userId
                }
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
            async {
                loadPostsUseCase(objective)
            }.await().collect {
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
                            PostsViewState(
                                isLoading = false,
                                error = null,
                                posts = it.data as List<Post>?
                            )
                    }
                    is Result.Error ->
                        _offerState.value =
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
                            PostsViewState(
                                isLoading = false,
                                error = null,
                                posts = it.data as List<Post>?
                            )
                    }
                    is Result.Error ->
                        _requestState.value =
                            PostsViewState(isLoading = false, error = it, posts = emptyList())
                }
            }
        }
    }

    override fun onLikeClicked(post: Post) {
        // If user is not signed in show Profile SignIN
        if (!_isSignedIn.value!!) {
            Timber.e("Showing Profile Sigin after LikeClicked")
            // _navigateToSignInDialogAction.value = Event(Unit)
            return
        }

        // If user is signed in.
        viewModelScope.launch {
            val data = likePostUsecase(post) // PostRequest
            getPosts(null)
            // getRequests("request")
        }
    }

    override fun onEditClicked(post: Post) {
        // TODO("Not yet implemented")
    }

    override /*suspend*/ fun onDeleteClicked(post: Post) {
        Timber.e("${post.title} Deleted Successfully")
        _isDeleted.value = Event("${post.title} Deleted Successfully")
        viewModelScope.launch {
            _isDeleted.value = Event("Post Deleted Successfully")
        }
    }

    override fun userId(): String? {
        return _userId.value
    }
}

/**
 * UI Model for [HomeFragment].
 */
data class PostsViewState(
    var isLoading: Boolean,
    val error: Result.Error?,
    val posts: List<Post>? = null,
)

interface HomeEventListener : EventActions {
    fun userId(): String?
}
