package com.fightpandemics.core.data.remote.login

import com.fightpandemics.core.data.model.login.ChangePasswordResponse
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.CompleteProfileResponse
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.model.login.SignUpResponse
import retrofit2.Response

interface LoginRemoteDataSource {

    suspend fun login(loginRequest: LoginRequest): Response<*>
    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>
    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
    suspend fun completeProfile(request: CompleteProfileRequest): Response<CompleteProfileResponse>
}
