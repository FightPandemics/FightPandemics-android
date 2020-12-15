package com.fightpandemics.core.data.model.profile

data class IndividualProfileResponse(
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
    val coordinates: List<Int>,
    val country: String,
    val neighborhood: String,
    val state: String,
    val type: String,
    val zip: String
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
    val facebook: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val twitter: String? = null,
    val github: String? = null,
    val website: String? = null,
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