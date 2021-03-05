package com.fightpandemics.core.data.model.post

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class PostDetailResponse(
    val post: PostDetail
)
@Parcelize
@Json(name = "post")
data class PostDetail(
    val __v: Int,
    val _id: String,
    val author: AuthorPostDetail,
    val commentsCount: Int,
    val content: String,
    val createdAt: String,
    val elapsedTimeText: ElapsedTimeTextPostDetail,
    val expireAt: String?,
    val isEdited: Boolean,
    val language: List<String>,
    val liked: Boolean,
    val likes: List<String>,
    val likesCount: Int,
    val objective: String,
    val reportsCount: Int,
    val status: String,
    val title: String,
    val types: List<String>,
    val updatedAt: String,
    val visibility: String
) : Parcelable

@Parcelize
@Json(name = "author")
data class AuthorPostDetail(
    val id: String,
    val location: LocationPostDetail,
    val name: String,
    val photo: String,
    val type: String
) : Parcelable

@Parcelize
@Json(name = "elapsedTimeText")
data class ElapsedTimeTextPostDetail(
    val created: CreatedPostDetail,
    val isEdited: Boolean
) : Parcelable

@Parcelize
@Json(name = "location")
data class LocationPostDetail(
    val city: String,
    val country: String,
    val state: String
) : Parcelable

@Parcelize
@Json(name = "created")
data class CreatedPostDetail(
    val count: Int,
    val unit: String
) : Parcelable
