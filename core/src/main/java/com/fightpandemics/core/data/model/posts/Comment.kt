package com.fightpandemics.core.data.model.posts

data class Comment(
    val __v: Int,
    val _id: String,
    val author: Author,
    val childCount: Int,
    val children: List<Any>,
    val content: String,
    val createdAt: String,
    val elapsedTimeText: ElapsedTimeText,
    val likes: List<Any>,
    val postId: String,
    val updatedAt: String
)