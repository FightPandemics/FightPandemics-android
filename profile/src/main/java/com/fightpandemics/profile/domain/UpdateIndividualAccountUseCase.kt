package com.fightpandemics.profile.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileResponse
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@FeatureScope
class UpdateIndividualAccountUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<PatchIndividualAccountRequest, Any>(dispatcherProvider.default) {

    @ExperimentalCoroutinesApi
    override suspend fun execute(parameters: PatchIndividualAccountRequest?): Flow<Result<Any>> {
        return channelFlow {
            profileRepository.updateIndividualUserAccount(parameters!!).collect {
                val result = when (it) {
                    is Result.Success -> it as Result<PatchIndividualProfileResponse>
                    is Result.Error -> it
                    else -> null
                }
                channel.offer(result as Result<Any>)
            }
        }
    }
}
