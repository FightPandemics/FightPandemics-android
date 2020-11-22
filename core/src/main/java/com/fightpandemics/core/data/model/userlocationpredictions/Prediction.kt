package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class Prediction(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "matched_substrings") val matched_substrings: List<MatchedSubstring>,
    @field:Json(name = "place_id") val place_id: String,
    @field:Json(name = "reference") val reference: String,
    @field:Json(name = "structured_formatting") val structured_formatting: StructuredFormatting,
    @field:Json(name = "terms") @Transient val terms: List<Term>,
    @field:Json(name = "types") @Transient val types: List<String>
)