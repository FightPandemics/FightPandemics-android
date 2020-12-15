package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.profile.ProfileRemoteDataSource
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ProfileRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override fun getInvididualUser(): Flow<Result<IndividualProfileResponse>> {
        return flow {
            val individualUser = profileRemoteDataSource.fetchCurrentUser()
            emit(Result.Success(individualUser))
        }
    }

}