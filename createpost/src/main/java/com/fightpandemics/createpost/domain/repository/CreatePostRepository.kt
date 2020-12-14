package com.fightpandemics.createpost.domain.repository

import com.fightpandemics.createpost.data.model.CreatePostRequest

interface CreatePostRepository {

    suspend fun createPost(createPostRequest: CreatePostRequest): Any
}