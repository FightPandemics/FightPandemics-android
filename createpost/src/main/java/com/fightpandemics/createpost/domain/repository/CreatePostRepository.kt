package com.fightpandemics.createpost.domain.repository

import com.fightpandemics.core.data.model.posts.Post

interface CreatePostRepository {

    suspend fun createPost(post: Post): Any
}