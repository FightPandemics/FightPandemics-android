package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.domain.usecase.SuspendUseCase
import com.fightpandemics.core.result.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FeatureScope
class DeletePostUsecase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
): FlowUseCase<Post, Any?>(dispatcherProvider.default) {

    override suspend fun execute(parameters: Post?): Flow<Result<Any?>> {
        return postsRepository.deletePost(parameters!!).map { results ->
            when (results) {
                is Result.Success -> results
                is Result.Error -> results
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}