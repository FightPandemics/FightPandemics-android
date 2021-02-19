package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Location(
    @field:Json(name = "address") val address: String?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "coordinates") val coordinates: List<Double>?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "zip") val zip: String?
)
