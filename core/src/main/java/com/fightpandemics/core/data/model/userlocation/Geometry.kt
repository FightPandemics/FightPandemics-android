package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class Geometry(
    @field:Json(name = "bounds") val bounds: Bounds?,
    @field:Json(name = "location") val location: Location_,
    @field:Json(name = "location_type") val location_type: String,
    @field:Json(name = "viewport") val viewport: Viewport?
)
