package com.fightpandemics.login.domain

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ActivityScope
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<LoginRequest, Any>(dispatcherProvider.io) {

    override suspend fun execute(parameters: LoginRequest?): Flow<Result<Any>> {
        return channelFlow {
            loginRepository.login(parameters)!!.collect {
                val result = when (it) {
                    is Result.Success -> it as Result<LoginResponse>
                    is Result.Error -> it
                    else -> null
                }
                channel.offer(result as Result<Any>)
            }
        }
    }
}
