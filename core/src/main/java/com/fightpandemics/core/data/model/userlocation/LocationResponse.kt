package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse(
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "original") @Transient val original: Original? = null
)
