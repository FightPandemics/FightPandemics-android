package com.fightpandemics.core.data.remote.posts

import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts

interface PostsRemoteDataSource {

    suspend fun fetchPosts(): Posts
    suspend fun fetchPosts(objective: String?): List<Post>
}