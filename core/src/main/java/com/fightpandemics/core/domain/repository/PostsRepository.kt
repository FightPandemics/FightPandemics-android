package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    fun getPosts(): Flow<Result<Posts>>

    suspend fun getPosts(objective: String?): Flow<Result<*>>

    suspend fun getPostsByAuthor(
        authorId: String
    ): Flow<Result<List<Post>>>

    suspend fun editPost(postRequest: PostRequest)

    suspend fun deletePost(post: Post)

    suspend fun likePost(post: Post)
}
