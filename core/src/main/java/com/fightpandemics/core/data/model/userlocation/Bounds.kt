package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class Bounds(
    @field:Json(name = "northeast") val northeast: Northeast,
    @field:Json(name = "southwest") val southwest: Southwest
)
