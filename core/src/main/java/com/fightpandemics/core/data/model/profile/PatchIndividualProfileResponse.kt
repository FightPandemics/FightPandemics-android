package com.fightpandemics.core.data.model.profile

data class PatchIndividualProfileResponse(
    val __t: String,
    val __v: Int,
    val _id: String,
    val about: String,
    val authId: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val hide: Hide,
    val lastName: String,
    val location: Location,
    val needs: Needs,
    val notifyPrefs: NotifyPrefs,
    val objectives: Objectives,
    val type: String,
    val updatedAt: String,
    val urls: Urls
)


