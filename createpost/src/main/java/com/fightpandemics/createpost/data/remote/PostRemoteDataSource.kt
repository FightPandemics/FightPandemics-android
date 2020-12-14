package com.fightpandemics.createpost.data.remote

import com.fightpandemics.createpost.data.model.CreatePostRequest

interface PostRemoteDataSource {

    suspend fun createPost(createPostRequest: CreatePostRequest): Any
}