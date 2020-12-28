package com.fightpandemics.createpost.data.remote

import com.fightpandemics.createpost.data.model.CreatePostRequest
import retrofit2.Response

interface PostRemoteDataSource {

    suspend fun createPost(createPostRequest: CreatePostRequest): Response<*>
}