package com.fightpandemics.login.domain

import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.model.login.SignUpResponse
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FeatureScope
class CompleteProfileUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<CompleteProfileRequest, Any>(dispatcherProvider.io) {
    override suspend fun execute(request: CompleteProfileRequest?):  Flow<Result<Any>> {
        return channelFlow {
            loginRepository.completeProfile(request!!)?.collect {
                val result = when (it) {
                    is Result.Success -> it as Result<SignUpResponse>
                    is Result.Error -> it
                    else -> null
                }
                channel.offer(result as Result<Any>)
            }
        }
    }
}

