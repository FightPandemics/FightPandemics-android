package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Original(
    @field:Json(name = "html_attributions") val html_attributions: List<Any>,
    @field:Json(name = "result") val result: Result,
    @field:Json(name = "status") val status: String
)
