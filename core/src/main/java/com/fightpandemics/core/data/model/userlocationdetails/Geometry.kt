package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Geometry(
    @field:Json(name = "location") val location: Location_
)
