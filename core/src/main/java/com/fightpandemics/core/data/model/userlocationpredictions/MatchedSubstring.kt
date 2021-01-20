package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class MatchedSubstring(
    @field:Json(name = "length") val length: Int,
    @field:Json(name = "offset") val offset: Int
)
