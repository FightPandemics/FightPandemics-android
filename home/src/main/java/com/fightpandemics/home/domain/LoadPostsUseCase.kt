package com.fightpandemics.home.domain

import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.data.model.posts.Posts
import com.fightpandemics.domain.repository.PostsRepository
import com.fightpandemics.domain.usecase.FlowUseCase
import com.fightpandemics.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class LoadPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<String, List<Post>>(dispatcherProvider.default) {

    override suspend fun execute(parameters: String?): Flow<Result<List<Post>>> {
        return postsRepository.getPosts(parameters).map { results ->
            Timber.e("3ERGHNDBHJDNHJD")
            when (results) {
                is Result.Success -> {

                    results
                }
                is Result.Error -> {

                    results
                }
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }

        }
    }
}