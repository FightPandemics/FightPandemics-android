package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Result(
    @field:Json(name = "address_components") val address_components: List<AddressComponent>,
    @field:Json(name = "formatted_address") val formatted_address: String,
    @field:Json(name = "geometry") val geometry: Geometry
)
