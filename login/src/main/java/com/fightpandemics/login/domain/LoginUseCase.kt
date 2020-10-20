package com.fightpandemics.login.domain

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.result.data
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
) : FlowUseCase<Any, LoginResponse>(dispatcherProvider.io) {

    override suspend fun execute(parameters: Any?): Flow<Result<LoginResponse>> {
        return channelFlow {
            loginRepository.login(parameters as LoginRequest).collect {
                if (it is Result.Success) {
                    channel.offer(Result.Success(it.data) as Result<LoginResponse>)
                } else {
                    channel.offer(Result.Error(Exception(it.data?.error)))
                }
            }
        }
    }
}