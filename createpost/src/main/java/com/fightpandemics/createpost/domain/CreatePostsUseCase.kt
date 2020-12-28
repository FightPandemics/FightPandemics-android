package com.fightpandemics.createpost.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class CreatePostsUseCase @Inject constructor(
    private val createPostRepository: CreatePostRepository,
    dispatcherProvider: CoroutinesDispatcherProvider) : FlowUseCase<CreatePostRequest, Any>(dispatcherProvider.default) {

    override suspend fun execute(parameters: CreatePostRequest?): Flow<Result<Any>> {
        return channelFlow {
            createPostRepository.createPost(parameters!!).collect {
                offer(it as Result<Any>)
            }
        }
    }
}