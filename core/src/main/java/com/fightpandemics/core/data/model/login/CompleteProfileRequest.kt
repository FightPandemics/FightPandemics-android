package com.fightpandemics.core.data.model.login

data class CompleteProfileRequest(
    val firstName: String,
    val hide: Hide,
    val lastName: String,
    val location: Location,
    val needs: Needs,
    val objectives: Objectives
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

data class Objectives(
    val donate: Boolean,
    val shareInformation: Boolean,
    val volunteer: Boolean
)
