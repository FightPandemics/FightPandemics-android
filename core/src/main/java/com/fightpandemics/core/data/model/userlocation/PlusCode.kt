package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class PlusCode(
    @field:Json(name = "compound_code") val compound_code: String?,
    @field:Json(name = "global_code") val global_code: String?
)
