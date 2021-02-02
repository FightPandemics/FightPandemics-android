package com.fightpandemics.core.data.model.profile

data class PatchIndividualProfileRequest(
    val about: String,
    val urls: RequestUrls
)

data class RequestUrls(
    val facebook: String,
    val github: String,
    val instagram: String,
    val linkedin: String,
    val twitter: String,
    val website: String
)
