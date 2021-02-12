package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class RefreshToken(
    val refresh_token: String
)

data class RefreshTokenResponse(
    @field:Json(name = "token") val access_token: String,
    @field:Json(name = "refresh_token") val refresh_token: String
)
