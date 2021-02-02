package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.login.ErrorResponse
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.profile.ProfileRemoteDataSource
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.parseErrorJsonResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ProfileRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val preferenceStorage: PreferenceStorage,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override fun getIndividualUser(): Flow<Result<IndividualProfileResponse>> {
        return flow {
            val individualUser = profileRemoteDataSource.fetchCurrentUser()
            emit(Result.Success(individualUser))
        }
    }

    override fun updateIndividualUserProfile(profileRequest: PatchIndividualProfileRequest): Flow<Result<*>> {
        return channelFlow {
            val response = profileRemoteDataSource.updateCurrentUser(profileRequest)
            when {
                response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK -> {
                    val profileResponse = response.body()
                    channel.offer(Result.Success(profileResponse))
                }
                response.code() == HttpURLConnection.HTTP_BAD_REQUEST -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                else -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override fun updateIndividualUserAccount(accountRequest: PatchIndividualAccountRequest): Flow<Result<*>> {
        return channelFlow {
            val response = profileRemoteDataSource.updateCurrentUserAccount(accountRequest)
            when {
                response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK -> {
                    val accountResponse = response.body()
                    channel.offer(Result.Success(accountResponse))
                }
                response.code() == HttpURLConnection.HTTP_BAD_REQUEST -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                else -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }
}
