package com.fightpandemics.core.data.remote.profile

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI,
) : ProfileRemoteDataSource {

    override suspend fun fetchCurrentUser(): IndividualProfileResponse {
        return fightPandemicsAPI.getCurrentUser()
    }

}