package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.model.login.ChangePasswordResponse
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.ErrorResponse
import com.fightpandemics.core.data.model.login.LoginRequest
import com.fightpandemics.core.data.model.login.LoginResponse
import com.fightpandemics.core.data.model.login.SignUpRequest
import com.fightpandemics.core.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.parseErrorJsonResponse
import com.fightpandemics.core.utils.parseJsonResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val authTokenLocalDataSource: AuthTokenLocalDataSource
) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest?): Flow<Result<*>> {
        return channelFlow {
            val response = loginRequest?.let { loginRemoteDataSource.login(it) }
            when {
                response!!.isSuccessful && response.code() == 200 -> {
                    val loginResponse = response.parseJsonResponse<LoginResponse>(moshi)
                    authTokenLocalDataSource.setToken(loginResponse?.token)
                    authTokenLocalDataSource.setUserId(loginResponse?.user?.id)
                    channel.offer(Result.Success(loginResponse))
                }
                response.code() == 401 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Flow<Result<*>> {
        return channelFlow {
            val response = signUpRequest.let { loginRemoteDataSource.signUp(it) }
            when {

                response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK -> {
                    val signUpResponse = response.body()
                    // authTokenLocalDataSource.setToken(signUpResponse?.token)
                    // TODO maybe we have to consume current user service form backend to get serID
                    // authTokenLocalDataSource.setUserId(loginResponse?.)
                    channel.offer(Result.Success(signUpResponse))
                }
                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ||
                    response.code() == HttpURLConnection.HTTP_BAD_REQUEST ||
                    response.code() == HttpURLConnection.HTTP_CONFLICT -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                else -> {
                    channel.offer(Result.Error(Exception(noResponse)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun completeProfile(request: CompleteProfileRequest): Flow<Result<*>> {
        return channelFlow {
            val response = request.let { loginRemoteDataSource.completeProfile(it) }
            when {
                response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK -> {
                    val signUpResponse = response.body()
                    // authTokenLocalDataSource.setToken(signUpResponse?.token)
                    // TODO maybe we have to consume current user service form backend to get serID
                    // authTokenLocalDataSource.setUserId(loginResponse?.)
                    channel.offer(Result.Success(signUpResponse))
                }

                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ||
                    response.code() == HttpURLConnection.HTTP_BAD_REQUEST ||
                    response.code() == HttpURLConnection.HTTP_CONFLICT -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }

                else -> {
                    channel.offer(Result.Error(Exception(noResponse)))
                }
            }
            awaitClose { }
        }
    }

    companion object {
        private const val noResponse = "No Response from Server"
    }
}
