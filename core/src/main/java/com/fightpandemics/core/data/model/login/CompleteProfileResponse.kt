package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class CompleteProfileResponse(
    @field:Json(name = "email") val email: String,

)