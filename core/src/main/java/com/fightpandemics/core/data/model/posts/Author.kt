package com.fightpandemics.core.data.model.posts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Author(
    val id: String?,
    val location: Location?,
    val name: String?,
    val photo: String?,
    val type: String?
) : Parcelable
