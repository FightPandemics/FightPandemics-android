package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class LoginResponse(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "emailVerified") val emailVerified: Boolean?,
    @field:Json(name = "token") val token: String,
    @field:Json(name = "user") val user: User?
)
