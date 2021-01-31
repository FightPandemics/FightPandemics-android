package com.fightpandemics.profile.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@FeatureScope
class LoadCurrentUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<String, IndividualProfileResponse>(dispatcherProvider.default) {

    override suspend fun execute(parameters: String?): Flow<Result<IndividualProfileResponse>> {
        return profileRepository.getIndividualUser().map { results ->
            when (results) {
                is Result.Success -> results
                is Result.Error -> results
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}
