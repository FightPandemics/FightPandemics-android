package com.fightpandemics.core.data.model.posts

data class Post(
    val _id: String,
    val author: Author?,
    val commentsCount: Int?,
    val content: String?,
    val distance: Double?,
    val expireAt: Any?,
    val externalLinks: ExternalLinks?,
    val language: List<String>?,
    val liked: Boolean?,
    val likesCount: Int?,
    val objective: String?,
    val title: String?,
    val types: List<String>?,
    val visibility: String?
)