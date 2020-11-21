package com.fightpandemics.core.data.model.userlocationpredictions

import com.squareup.moshi.Json

data class LocationPrediction(
    @field:Json(name = "predictions") val predictions: List<Prediction>,
    @field:Json(name = "status") val status: String
)