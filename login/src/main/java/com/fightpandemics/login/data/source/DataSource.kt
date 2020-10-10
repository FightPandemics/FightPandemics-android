package com.fightpandemics.login.data.source

import com.fightpandemics.login.networking.*
import retrofit2.Response

interface DataSource {

    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
}