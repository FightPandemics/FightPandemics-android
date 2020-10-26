package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.domain.usecase.UseCase
import javax.inject.Inject

@FeatureScope
class UpdatePostUsecase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : UseCase<PostRequest, Any>(dispatcherProvider.default)  {

    override fun execute(parameters: PostRequest): Any {
        TODO("Not yet implemented")
    }
}