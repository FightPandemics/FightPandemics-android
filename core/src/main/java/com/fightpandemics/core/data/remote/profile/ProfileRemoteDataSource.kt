package com.fightpandemics.core.data.remote.profile

import com.fightpandemics.core.data.model.profile.IndividualProfileResponse

interface ProfileRemoteDataSource {

    suspend fun fetchCurrentUser(): IndividualProfileResponse

}