package com.fightpandemics.domain.repository

import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.data.model.posts.Posts
import kotlinx.coroutines.flow.Flow
import com.fightpandemics.result.Result

interface PostsRepository {

    fun getPosts(): Flow<Result<Posts>>

    suspend fun getPosts(objective: String?): Flow<Result<List<Post>>>
}