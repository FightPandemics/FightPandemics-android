package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.PostsRepository
import javax.inject.Inject

@FeatureScope
class DeletePostUsecase @Inject constructor(
    private val postsRepository: PostsRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
)
