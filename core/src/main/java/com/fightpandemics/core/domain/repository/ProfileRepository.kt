package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getInvididualUser(): Flow<Result<IndividualProfileResponse>>

}