package com.fightpandemics.core.data.model.userlocationdetails

import com.squareup.moshi.Json

data class Location(
    @Json(name = "address") private val addressPrivate: String?,
    @Json(name = "city") private val cityPrivate: String?,
    @Json(name = "coordinates") private val coordinatesPrivate: List<Double>?,
    @Json(name = "country") private val countryPrivate: String?,
    @Json(name = "state") private val statePrivate: String?,
    @Json(name = "zip") private val zipPrivate: String?
) {
    val address: String
        get() = addressPrivate ?: ""

    val city
        get() = cityPrivate ?: ""

    val coordinates
        get() = coordinatesPrivate ?: listOf()

    val country
        get() = countryPrivate ?: ""

    val state
        get() = statePrivate ?: ""

    val zip
        get() = zipPrivate ?: ""
}
