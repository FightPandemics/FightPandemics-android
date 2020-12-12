package com.fightpandemics.createpost.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.domain.usecase.SuspendUseCase
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import javax.inject.Inject

@FeatureScope
class CreatePostsUseCase @Inject constructor(
    private val createPostRepository: CreatePostRepository,
    dispatcherProvider: CoroutinesDispatcherProvider) : SuspendUseCase<Post, Any>(dispatcherProvider.default) {

    override suspend fun execute(parameters: Post): Any {
        return createPostRepository.createPost(parameters)
    }
}