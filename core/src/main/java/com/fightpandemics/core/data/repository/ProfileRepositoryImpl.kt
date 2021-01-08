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
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ProfileRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val preferenceStorage: PreferenceStorage,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override fun getInvididualUser(): Flow<Result<IndividualProfileResponse>> {
        return flow {
            val individualUser = profileRemoteDataSource.fetchCurrentUser()
            emit(Result.Success(individualUser))
        }
    }

    override fun updateInvididualUserProfile(profileRequest: PatchIndividualProfileRequest): Flow<Result<*>> {
        return channelFlow {
            val response = profileRemoteDataSource.updateCurrertUser(profileRequest)
            when {
                response.isSuccessful && response.code() == 200 -> {
                    val profileResponse = response.body()
                    channel.offer(Result.Success(profileResponse))
                }
                response.code() == 400 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                else ->{
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override fun updateInvididualUserAccount(accountRequest: PatchIndividualAccountRequest): Flow<Result<*>> {
        return channelFlow {
            val response = profileRemoteDataSource.updateCurrertUserAccount(accountRequest)
            when {
                response.isSuccessful && response.code() == 200 -> {
                    val accountResponse = response.body()
                    channel.offer(Result.Success(accountResponse))
                }
                response.code() == 400 -> {
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
                else ->{
                    val myError = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }
}