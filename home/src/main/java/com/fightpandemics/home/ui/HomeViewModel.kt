package com.fightpandemics.home.ui

import androidx.lifecycle.*
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.domain.LoadPostsUseCase
import com.fightpandemics.result.Result
import com.fightpandemics.result.data
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class HomeViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    val s: String = "SAME"

    private val _postsState = MutableLiveData<PostsViewState>()
    val postsState: LiveData<PostsViewState> get() = _postsState

    init {
//        getPosts(null)
//        getPosts("request")
//        getPosts("offer")
    }

    fun getPosts(objective: String?): Job {
        // Set a default loading state
        _postsState.value?.isLoading = true
        return viewModelScope.launch {
            loadPostsUseCase(objective)
                .collect {
                    withContext(dispatcherProvider.main) {
                        when (it) {
                            is Result.Success -> _postsState.value =
                                PostsViewState(isLoading = false, error = null, posts = it.data)
                            is Result.Error -> _postsState.value =
                                PostsViewState(isLoading = false, error = it, posts = emptyList())
                        }
                    }
                }
        }
    }

//    fun retry() {
//        getPosts()
//    }
//    fun getEvolutions(pokeId: Int) {
//        getEvolutionsNow(pokeId)
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
