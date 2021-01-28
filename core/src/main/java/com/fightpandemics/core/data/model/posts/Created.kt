package com.fightpandemics.core.data.model.posts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Created(
    val count: Int?,
    val unit: String?
) : Parcelable