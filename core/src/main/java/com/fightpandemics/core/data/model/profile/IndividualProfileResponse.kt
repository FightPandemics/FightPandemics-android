package com.fightpandemics.core.data.model.profile

data class IndividualProfileResponse(
    val about: String,
    val email: String,
    val firstName: String,
    val hide: Hide,
    val id: String,
    val lastName: String,
    val location: Location,
    val needs: Needs,
    val notifyPrefs: NotifyPrefs,
    val objectives: Objectives,
    val organisations: List<Any>,
    val urls: Urls,
    val usesPassword: Boolean
)

data class Hide(
    val address: Boolean
)

data class Location(
    val address: String,
    val city: String,
    val coordinates: List<Double>,
    val country: String,
    val state: String
)

data class Needs(
    val medicalHelp: Boolean,
    val otherHelp: Boolean
)

data class NotifyPrefs(
    val _id: String,
    val digest: Digest,
    val instant: Instant
)

data class Objectives(
    val donate: Boolean,
    val shareInformation: Boolean,
    val volunteer: Boolean
)

data class Urls(
    val facebook: String,
    val github: String,
    val instagram: String,
    val linkedin: String,
    val twitter: String,
    val website: String
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