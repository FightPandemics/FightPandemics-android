package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/*
* created by Osaigbovo Odiase
* */
interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest?): Flow<Result<*>>?

    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse>

    suspend fun changePassword(email: String): Response<ChangePasswordResponse>
}
