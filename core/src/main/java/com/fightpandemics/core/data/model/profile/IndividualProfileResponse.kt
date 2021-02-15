package com.fightpandemics.core.data.model.profile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndividualProfileResponse(
    val about: String?,
    val email: String,
    val firstName: String,
    val hide: Hide,
    val id: String,
    val lastName: String,
    val location: Location,
    val needs: Needs,
    val notifyPrefs: NotifyPrefs,
    val objectives: Objectives,
    val organisations: List<Organisation>,
    val photo: String?,
    val urls: Urls,
    val usesPassword: Boolean
) : Parcelable

@Parcelize
data class Hide(
    val address: Boolean
) : Parcelable

@Parcelize
data class Location(
    val address: String,
    val city: String,
    val coordinates: List<Double>,
    val country: String,
    val state: String
) : Parcelable

@Parcelize
data class Needs(
    val medicalHelp: Boolean,
    val otherHelp: Boolean
) : Parcelable

@Parcelize
data class NotifyPrefs(
    @Json(name = "_id") val backendID: String,
    val digest: Digest,
    val instant: Instant
) : Parcelable

@Parcelize
data class Objectives(
    val donate: Boolean,
    val shareInformation: Boolean,
    val volunteer: Boolean
) : Parcelable

@Parcelize
data class Urls(
    val facebook: String?,
    val github: String?,
    val instagram: String?,
    val linkedin: String?,
    val twitter: String?,
    val website: String?
) : Parcelable

@Parcelize
data class Digest(
    val biweekly: Boolean,
    val daily: Boolean,
    val weekly: Boolean
) : Parcelable

@Parcelize
data class Instant(
    val comment: Boolean,
    val like: Boolean,
    val message: Boolean,
    val share: Boolean
) : Parcelable

@Parcelize
data class Organisation(
    @Json(name = "_id") val backendID: String,
    val createdAt: String,
    val email: String,
    val global: Boolean,
    val industry: String,
    val location: Location,
    val name: String,
    val needs: Needs,
    val notifyPrefs: NotifyPrefs,
    val ownerId: String,
    val t: String,
    val type: String,
    val updatedAt: String,
    val v: Int
) : Parcelable
