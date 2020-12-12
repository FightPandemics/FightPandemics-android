package com.fightpandemics.createpost.data.remote

import com.fightpandemics.core.data.model.posts.Post

interface PostRemoteDataSource {

    suspend fun createPost(post: Post): Any
}