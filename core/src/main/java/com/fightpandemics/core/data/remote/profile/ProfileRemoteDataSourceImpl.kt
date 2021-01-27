package com.fightpandemics.core.data.remote.profile

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI,
) : ProfileRemoteDataSource {

    override suspend fun fetchCurrentUser(): IndividualProfileResponse {
        return fightPandemicsAPI.getCurrentUser()
    }

    override suspend fun updateCurrertUser(patchIndividualProfileRequest: PatchIndividualProfileRequest): Response<PatchIndividualProfileResponse> =
        fightPandemicsAPI.updateCurrentUserProfile(patchIndividualProfileRequest)

    override suspend fun updateCurrertUserAccount(patchIndividualAccountRequest: PatchIndividualAccountRequest): Response<PatchIndividualProfileResponse> =
        fightPandemicsAPI.updateCurrentUserAccount(patchIndividualAccountRequest)
}
