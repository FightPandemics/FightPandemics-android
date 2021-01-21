package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class Prediction(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "matched_substrings") @Transient val matched_substrings: List<MatchedSubstring>? = null,
    @field:Json(name = "place_id") @Transient val place_id: String? = null,
    @field:Json(name = "reference") @Transient val reference: String? = null,
    @field:Json(name = "structured_formatting") @Transient val structured_formatting: StructuredFormatting? = null,
    @field:Json(name = "terms") @Transient val terms: List<Term>? = null,
    @field:Json(name = "types") @Transient val types: List<String>? = null
)
