package com.fightpandemics.core.data.model.profile

import com.squareup.moshi.Json

data class PatchIndividualProfileResponse(
    @Json(name = "__t") val backendT: String,
    @Json(name = "__v") val backendV: Int,
    @Json(name = "_id") val backendID: String,
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
