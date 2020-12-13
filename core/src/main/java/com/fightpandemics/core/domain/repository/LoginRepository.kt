package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest?): Flow<Result<*>>?


    suspend fun signUp(signUpRequest: SignUpRequest): Flow<Result<*>>?

    suspend fun changePassword(email: String): Response<ChangePasswordResponse>

    suspend fun completeProfile(request: CompleteProfileRequest): Flow<Result<*>>?
}