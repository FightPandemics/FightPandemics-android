package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Location_(
    @field:Json(name = "lat") val lat: Double,
    @field:Json(name = "lng") val lng: Double
)
