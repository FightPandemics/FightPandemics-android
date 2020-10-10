package com.fightpandemics.login.data.repository

import com.fightpandemics.login.networking.*
import retrofit2.Response

interface DataRepository {

    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
}