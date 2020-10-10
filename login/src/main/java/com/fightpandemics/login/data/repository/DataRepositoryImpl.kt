package com.fightpandemics.login.data.repository

import com.fightpandemics.login.data.source.local.LocalDataSource
import com.fightpandemics.login.data.source.remote.RemoteDataSource
import com.fightpandemics.login.networking.*
import retrofit2.Response

class DataRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : DataRepository {

    override suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        return remoteDataSource.signUp(signUpRequest)
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return remoteDataSource.login(loginRequest)
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }
}