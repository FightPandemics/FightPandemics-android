package com.fightpandemics.core.data.remote.posts

import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import retrofit2.Response

interface PostsRemoteDataSource {

    suspend fun fetchPosts(): Posts

    suspend fun fetchPosts(objective: String?): Response<List<Post>>

    suspend fun fetchPostsByAuthor(authorId: String): List<Post>

    suspend fun updatePost(postId: String, postRequest: PostRequest)

    suspend fun deletePost(postId: String)

    suspend fun likePost(postId: String, userId: String, like: Boolean)
}
