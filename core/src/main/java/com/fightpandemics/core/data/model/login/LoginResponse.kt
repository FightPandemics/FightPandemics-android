package com.fightpandemics.core.data.model.login

import DataClasses.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class LoginResponse(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "emailVerified") val emailVerified: Boolean?,
    @field:Json(name = "token") val token: String,
    @field:Json(name = "user") val user: User?
)