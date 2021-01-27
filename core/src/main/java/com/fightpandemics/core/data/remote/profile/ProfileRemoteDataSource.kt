package com.fightpandemics.core.data.remote.profile

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileResponse
import retrofit2.Response

interface ProfileRemoteDataSource {

    suspend fun fetchCurrentUser(): IndividualProfileResponse

    suspend fun updateCurrertUser(patchIndividualProfileRequest: PatchIndividualProfileRequest): Response<PatchIndividualProfileResponse>

    suspend fun updateCurrertUserAccount(patchIndividualAccountRequest: PatchIndividualAccountRequest): Response<PatchIndividualProfileResponse>
}
