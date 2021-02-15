package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getIndividualUser(): Flow<Result<IndividualProfileResponse>>

    fun updateIndividualUserProfile(profileRequest: PatchIndividualProfileRequest): Flow<Result<*>>

    fun updateIndividualUserAccount(accountRequest: PatchIndividualAccountRequest): Flow<Result<*>>
}
