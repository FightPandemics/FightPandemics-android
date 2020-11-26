package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationPrediction(
    @field:Json(name = "predictions") val predictions: List<Prediction>,
    @field:Json(name = "status") @Transient val status: String? = null
)
