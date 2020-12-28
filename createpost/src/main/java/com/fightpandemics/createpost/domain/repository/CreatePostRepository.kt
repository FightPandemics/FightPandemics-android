package com.fightpandemics.createpost.domain.repository

import com.fightpandemics.core.result.Result
import com.fightpandemics.createpost.data.model.CreatePostRequest
import kotlinx.coroutines.flow.Flow

interface CreatePostRepository {

    suspend fun createPost(createPostRequest: CreatePostRequest): Flow<Result<*>>
}