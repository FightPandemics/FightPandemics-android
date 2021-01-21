package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class Original(
    @field:Json(name = "plus_code") val plus_code: PlusCode?,
    @field:Json(name = "results") val results: List<Result>?,
    @field:Json(name = "status") val status: String?
)
