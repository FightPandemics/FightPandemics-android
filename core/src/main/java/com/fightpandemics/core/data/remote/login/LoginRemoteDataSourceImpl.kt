package com.fightpandemics.core.data.remote.login

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI
) : LoginRemoteDataSource {

    @ExperimentalCoroutinesApi
    override suspend fun login(loginRequest: LoginRequest): Response<*> {
        return fightPandemicsAPI.login(loginRequest)
    }


    override suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> =
        fightPandemicsAPI.signUp(signUpRequest)

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> =
        fightPandemicsAPI.changePassword(email)

    override suspend fun completeProfile(request: CompleteProfileRequest): Response<CompleteProfileResponse> {
        return fightPandemicsAPI.completeProfile(request)
    }
}