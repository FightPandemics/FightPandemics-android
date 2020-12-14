package com.fightpandemics.createpost.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.usecase.SuspendUseCase
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import javax.inject.Inject

@FeatureScope
class CreatePostsUseCase @Inject constructor(
    private val createPostRepository: CreatePostRepository,
    dispatcherProvider: CoroutinesDispatcherProvider) : SuspendUseCase<CreatePostRequest, Any>(dispatcherProvider.default) {

    override suspend fun execute(parameters: CreatePostRequest): Any {
        return createPostRepository.createPost(parameters)
    }
}