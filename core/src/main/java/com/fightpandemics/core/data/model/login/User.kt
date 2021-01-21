package com.fightpandemics.core.data.model.login

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "firstName") val firstName: String,
    @field:Json(name = "id") val id: String,
    @field:Json(name = "lastName") val lastName: String,
    @field:Json(name = "organisations") val organisations: List<Any>
)
