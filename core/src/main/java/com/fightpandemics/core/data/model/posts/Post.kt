package com.fightpandemics.core.data.model.posts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val _id: String,
    val author: Author?,
    val commentsCount: Int?,
    val content: String?,
    val distance: Double?,
    val expireAt: String?,
    val externalLinks: ExternalLinks?,
    val language: List<String>?,
    var liked: Boolean?,
    val likesCount: Int?,
    val objective: String?,
    val title: String?,
    val types: List<String>?,
    val visibility: String?
) : Parcelable
