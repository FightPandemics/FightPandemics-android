package com.fightpandemics.createpost.data.model

data class CreatePostRequest(
    val title: String?,
    val content: String?,
    val types: List<String>?,
    val visibility: String?,
    val expireAt: Any?,
    val objective: String?
)