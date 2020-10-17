package com.fightpandemics.login.data.source.remote

import com.fightpandemics.login.data.source.DataSource
import com.fightpandemics.login.networking.*
import retrofit2.Response

class RemoteDataSource (private val apiService: ApiService) : DataSource {

    override suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        return apiService.signUp(signUpRequest)
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        return apiService.changePassword(email)
    }
}