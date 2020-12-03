package com.fightpandemics.core.data.model.login

import DataClasses.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class CompleteProfileRequest(
    @field:Json(name = "firstName") val firstName: String,
    @field:Json(name = "hide") val hide: Hide,
    @field:Json(name = "lastName") val lastName: String,
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "needs") val needs: Needs,
    @field:Json(name = "notifyPrefs") val notifyPrefs: NotifyPrefs,
    @field:Json(name = "objectives") val objectives: Objectives,
    @field:Json(name = "url") val url: Url
)

data class Hide(
    @field:Json(name = "url")  val address: Boolean
)

data class Location(
    @field:Json(name = "address") val address: String,
    @field:Json(name = "city") val city: String,
    @field:Json(name = "coordinates") val coordinates: List<Int>,
    @field:Json(name = "country") val country: String,
    @field:Json(name = "neighborhood") val neighborhood: String,
    @field:Json(name = "state") val state: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "zip") val zip: String
)

data class Needs(
    @field:Json(name = "medicalHelp") val medicalHelp: Boolean,
    @field:Json(name = "otherHelp") val otherHelp: Boolean
)

data class NotifyPrefs(
    @field:Json(name = "digest") val digest: Digest,
    @field:Json(name = "instant") val instant: Instant
)

data class Objectives(
    @field:Json(name = "donate") val donate: Boolean,
    @field:Json(name = "shareInformation") val shareInformation: Boolean,
    @field:Json(name = "volunteer") val volunteer: Boolean
)

class Url(
)

data class Digest(
    @field:Json(name = "biweekly") val biweekly: Boolean,
    @field:Json(name = "daily") val daily: Boolean,
    @field:Json(name = "weekly") val weekly: Boolean
)

data class Instant(
    @field:Json(name = "comment") val comment: Boolean,
    @field:Json(name = "like") val like: Boolean,
    @field:Json(name = "message") val message: Boolean,
    @field:Json(name = "share") val share: Boolean
)