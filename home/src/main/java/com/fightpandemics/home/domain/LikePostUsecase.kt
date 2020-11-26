package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.usecase.SuspendUseCase
import javax.inject.Inject

@FeatureScope
class LikePostUsecase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : SuspendUseCase<Post, Any>(dispatcherProvider.default) {

    override suspend fun execute(parameters: Post): Any {
        return postsRepository.likePost(parameters)
    }
}
