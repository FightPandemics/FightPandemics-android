package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@FeatureScope
class LoadPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<String, Post>(dispatcherProvider.default) {

    override suspend fun execute(parameters: String?): Flow<Result<Post>> {
        return postsRepository.getPost(postId = parameters!!).map { results ->
            when (results) {
                is Result.Success -> results
                is Result.Error -> results
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}