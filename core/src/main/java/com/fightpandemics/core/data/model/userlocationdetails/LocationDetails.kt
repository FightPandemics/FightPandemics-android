package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class LocationDetails(
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "original") val original: Original
)
