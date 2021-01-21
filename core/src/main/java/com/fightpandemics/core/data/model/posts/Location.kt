package com.fightpandemics.core.data.model.posts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val city: String?,
    val state: String?,
    val country: String?,
) : Parcelable
