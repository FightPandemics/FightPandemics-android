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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@FeatureScope
class HomeViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    val s: String = "SAME"

    private val _postsState = MutableLiveData<PostsViewState>()
    val postsState: LiveData<PostsViewState> get() = _postsState

    private val _offerState = MutableLiveData<PostsViewState>()
    val offerState: LiveData<PostsViewState> get() = _offerState

    private val _requestState = MutableLiveData<PostsViewState>()
    val requestState: LiveData<PostsViewState> get() = _requestState

    init {
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


//            loadPostsUseCase(objective)
//                .collect {
//
//                    withContext(dispatcherProvider.main) {
//                        when (it) {
//                            is Result.Success -> {
//                                Timber.e(it.data.get(0).author?.name.toString())
//                                _postsState.value = PostsViewState(isLoading = false, error = null, posts = it.data)
//
//                            }
//                            is Result.Error -> _postsState.value =
//                                PostsViewState(isLoading = false, error = it, posts = emptyList())
//                        }
//                    }
//                }


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

//            loadPostsUseCase(objective)
//                .collect {
//                        when (it) {
//                            is Result.Success -> {
//                                Timber.e(it.data.get(0).author?.name.toString())
//                                _offerState.value = OffersViewState(isLoading = false, error = null, posts = it.data)
//
//                            }
//                            is Result.Error -> _offerState.value =
//                                OffersViewState(isLoading = false, error = it, posts = emptyList())
//                        }
//                }
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

//    fun retry() {
//        getPosts()
//    }

}

/**
 * UI Model for [HomeFragment].
 */
data class PostsViewState(
    var isLoading: Boolean,
    val error: Result.Error?,
    val posts: List<Post>?,
)
