package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class LocationResponse(
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "original") val original: Original
)