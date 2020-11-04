package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.domain.usecase.UseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FeatureScope
class ObserveUserAuthStateUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val dispatcherProvider: CoroutinesDispatcherProvider
): UseCase<Any, Boolean>(dispatcherProvider.default) {

    override fun execute(parameters: Any): Boolean = preferenceStorage.token != null
}