package com.fightpandemics.core.data.model.post

import com.fightpandemics.core.data.model.posts.Author

data class CreatePostResponse(
    val isEdited: Boolean?,
    val language: List<String>?,
    val likes: List<Any>?,
    val status: String?,
    val types: List<String>?,
    val _id: String?,
    val title: String?,
    val content: String?,
    val visibility: String?,
    val expireAt: Any?,
    val objective: String?,
    val author: Author?,
    val reportedBy: List<Any>?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)