package com.fightpandemics.domain.repository

import com.fightpandemics.data.model.login.*
import com.fightpandemics.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse?>>


    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>

    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
}