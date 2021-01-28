package com.fightpandemics.core.data.model.posts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ElapsedTimeText(
    val created: Created?,
    val isEdited: Boolean?
) : Parcelable