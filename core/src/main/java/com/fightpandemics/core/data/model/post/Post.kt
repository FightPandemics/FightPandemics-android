package com.fightpandemics.core.data.model.post

data class Post(
    val _id: String,
    val airtableId: String,
    val author: Author,
    val content: String,
    val createdAt: String,
    val externalLinks: ExternalLinks,
    val language: List<String>,
    val liked: Boolean,
    val likes: List<Any>,
    val likesCount: Int,
    val objective: String,
    val title: String,
    val types: List<String>,
    val updatedAt: String,
    val visibility: String
)
