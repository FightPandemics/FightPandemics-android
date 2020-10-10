package com.fightpandemics.login.data.source.local

import com.fightpandemics.login.data.source.DataSource
import com.fightpandemics.login.networking.*
import retrofit2.Response

class LocalDataSource : DataSource {
    override suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }
}