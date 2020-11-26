package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class StructuredFormatting(
    @field:Json(name = "main_text") val main_text: String,
    @field:Json(name = "main_text_matched_substrings")
    val main_text_matched_substrings: List<MainTextMatchedSubstring>,
    @field:Json(name = "secondary_text") val secondary_text: String
)
