package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class CompleteProfileResponse(
    val __t: String,
    val __v: Int,
    val _id: String,
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
    val organisations: List<Any>,
    val type: String,
    val updatedAt: String
)

@Json(name = "Hide")
data class HideResponse(
    val address: Boolean
)
@Json(name = "Location")
data class LocationResponse(
    val address: String,
    val city: String,
    val coordinates: List<Int>,
    val country: String,
    val state: String
)
@Json(name = "Needs")
data class NeedsResponse(
    val medicalHelp: Boolean,
    val otherHelp: Boolean
)

data class NotifyPrefs(
    val _id: String,
    val digest: Digest,
    val instant: Instant
)
@Json(name = "Objectives")
data class Objectivesesponse(
    val donate: Boolean,
    val shareInformation: Boolean,
    val volunteer: Boolean
)

data class Digest(
    val biweekly: Boolean,
    val daily: Boolean,
    val weekly: Boolean
)

data class Instant(
    val comment: Boolean,
    val like: Boolean,
    val message: Boolean,
    val share: Boolean
)