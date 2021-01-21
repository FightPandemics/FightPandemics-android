package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class SignUpResponse(
    @field:Json(name = "emailVerified") val emailVerified: Boolean,
    @field:Json(name = "token") val token: String
)
