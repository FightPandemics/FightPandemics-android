package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getInvididualUser(): Flow<Result<IndividualProfileResponse>>

    fun updateInvididualUserProfile(profileRequest: PatchIndividualProfileRequest): Flow<Result<*>>

    fun updateInvididualUserAccount(accountRequest: PatchIndividualAccountRequest): Flow<Result<*>>

}