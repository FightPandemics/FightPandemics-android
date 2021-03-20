package com.fightpandemics.core.data.model.userlocation

import com.squareup.moshi.Json

data class Location(
    @Json(name = "address") private val addressPrivate: String?,
    @Json(name = "city") private val cityPrivate: String?,
    @Json(name = "coordinates") private val coordinatesPrivate: List<Double?>?,
    @Json(name = "country") private val countryPrivate: String?,
    @Json(name = "neighborhood") private val neighborhoodPrivate: String?,
    @Json(name = "state") private val statePrivate: String?
) {
    val address
        get() = addressPrivate ?: ""

    val city
        get() = cityPrivate ?: ""

    val coordinates: List<Double>
        get() = listOf(
            coordinatesPrivate?.get(0) ?: 0.0,
            coordinatesPrivate?.get(1) ?: 0.0
        )

    val country
        get() = countryPrivate ?: ""

    val neighborhood
        get() = neighborhoodPrivate ?: ""

    val state
        get() = statePrivate ?: ""
}
