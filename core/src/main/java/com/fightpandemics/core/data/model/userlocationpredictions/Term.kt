package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class Term(
    @field:Json(name = "offset") val offset: Int,
    @field:Json(name = "value") val value: String
)
