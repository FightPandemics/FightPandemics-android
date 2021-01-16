package com.fightpandemics.home.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.usecase.UseCase
import javax.inject.Inject

@FeatureScope
class ObserveUserAuthStateUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : UseCase<Any, UserInfo>(dispatcherProvider.default) {

    override fun execute(parameters: Any): UserInfo {
        return UserInfo(
            preferenceStorage.token != null,
            preferenceStorage.userId
        )
    }
}

data class UserInfo(
    var signedIn: Boolean,
    var userId: String?,
)
