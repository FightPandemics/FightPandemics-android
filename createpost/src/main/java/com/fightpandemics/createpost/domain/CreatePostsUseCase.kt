package com.fightpandemics.createpost.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.post.CreatePostRequest
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class CreatePostsUseCase @Inject constructor(
    private val createPostRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider
) : FlowUseCase<CreatePostRequest, Any>(dispatcherProvider.default) {

    @InternalCoroutinesApi
    override suspend fun execute(parameters: CreatePostRequest?): Flow<Result<Any>> {
        return channelFlow {
            createPostRepository.createPost(parameters!!).collect {
                offer(it as Result<Any>)
            }
        }
    }
}
