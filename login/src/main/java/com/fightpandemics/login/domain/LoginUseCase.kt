package com.fightpandemics.login.domain

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.model.login.LoginRequest
import com.fightpandemics.data.model.login.LoginResponse
import com.fightpandemics.domain.repository.LoginRepository
import com.fightpandemics.domain.usecase.FlowUseCase
import com.fightpandemics.result.Result
import com.fightpandemics.result.data
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