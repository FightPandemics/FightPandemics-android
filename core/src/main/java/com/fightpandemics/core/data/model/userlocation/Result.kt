package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class Result(
    @field:Json(name = "address_components") val address_components: List<AddressComponent>?,
    @field:Json(name = "formatted_address") val formatted_address: String?,
    @field:Json(name = "geometry") val geometry: Geometry?,
    @field:Json(name = "place_id") val place_id: String?,
    @field:Json(name = "plus_code") val plus_code: PlusCode?,
    @field:Json(name = "types") val types: List<String?>?
)
