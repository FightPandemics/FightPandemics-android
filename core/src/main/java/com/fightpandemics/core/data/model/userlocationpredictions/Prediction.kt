package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class Prediction(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "matched_substrings") @Transient val matchedSubstrings: List<MatchedSubstring>? = null,
    @Json(name = "place_id") val placeId: String,
    @field:Json(name = "reference") @Transient val reference: String? = null,
    @field:Json(name = "structured_formatting") @Transient val structuredFormatting: StructuredFormatting? = null,
    @field:Json(name = "terms") @Transient val terms: List<Term>? = null,
    @field:Json(name = "types") @Transient val types: List<String>? = null
)
