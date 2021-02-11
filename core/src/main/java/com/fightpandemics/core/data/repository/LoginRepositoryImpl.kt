package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.model.login.*
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
import timber.log.Timber
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

                    if (loginResponse?.user != null) {
                        authTokenLocalDataSource.setUserId(loginResponse.user.id)
                        authTokenLocalDataSource.setUserFirstName(loginResponse.user.firstName)
                        authTokenLocalDataSource.setUserLastName(loginResponse.user.lastName)
                        authTokenLocalDataSource.setUserEmail(loginResponse.user.email)
                        authTokenLocalDataSource.setUserOrganisation(loginResponse.user.organisations)
                    }

                    channel.offer(Result.Success(loginResponse))
                }
                response.code() == 400 || response.code() == 401 || response.code() == 409 -> {
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
                response.isSuccessful && response.code() == 200 -> {
                    val signUpResponse = response.body()
                    Timber.e(signUpResponse.toString())
                    //authTokenLocalDataSource.setToken(signUpResponse?.token)
                    //TODO maybe we have to consume current user service form backend to get serID
                    //authTokenLocalDataSource.setUserId(loginResponse?.)
                    channel.offer(Result.Success(signUpResponse))
                }
                response.code() == 400 || response.code() == 401 || response.code() == 409 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun completeProfile(request: CompleteProfileRequest): Flow<Result<*>>? {
        return channelFlow {
            val response = request?.let { loginRemoteDataSource.completeProfile(it) }
            when {

                response!!.isSuccessful && response.code() == 200 -> {
                    val signUpResponse = response.body()
                    //authTokenLocalDataSource.setToken(signUpResponse?.token)
                    //TODO maybe we have to consume current user service form backend to get serID
                    //authTokenLocalDataSource.setUserId(loginResponse?.)
                    channel.offer(Result.Success(signUpResponse))
                }

                response.code() == 500 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                response.code() == 401 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                response.code() == 400 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                response.code() == 409 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun changePassword(email: String): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }
}