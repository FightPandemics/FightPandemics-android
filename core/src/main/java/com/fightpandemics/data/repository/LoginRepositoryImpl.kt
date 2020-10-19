package com.fightpandemics.data.repository

import com.fightpandemics.data.model.login.*
import com.fightpandemics.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.domain.repository.LoginRepository
import kotlinx.coroutines.channels.awaitClose
import com.fightpandemics.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginRepositoryImpl @Inject constructor(
    /*private val localDataSource: LocalDataSource,*/
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository {


    override suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse?>> =
        channelFlow {
            val loginResponse = loginRemoteDataSource.login(loginRequest)
            if(loginResponse.isSuccessful){
                // TODO - save user info in sharedpreference
                channel.offer(
                    Result.Success(loginResponse.body())
                )
            } else{
                channel.offer(Result.Error(Exception(loginResponse.errorBody().toString())))
            }
            awaitClose { }
        }


    override suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        return loginRemoteDataSource.signUp(signUpRequest)
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }
}