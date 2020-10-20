package com.fightpandemics.core.data.remote.login

import com.fightpandemics.core.data.model.login.*
import retrofit2.Response

interface LoginRemoteDataSource {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>
    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
}