package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class AddressComponent(
    @field:Json(name = "long_name") val long_name: String,
    @field:Json(name = "short_name") val short_name: String,
    @field:Json(name = "types") val types: List<String>
)
