package com.fightpandemics.createpost.data.remote

import com.fightpandemics.createpost.data.api.NetworkApi
import com.fightpandemics.createpost.data.model.CreatePostRequest
import retrofit2.Response
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(private val networkApi: NetworkApi) : PostRemoteDataSource {

    override suspend fun createPost(createPostRequest: CreatePostRequest): Response<*> {
        return networkApi.createPost(createPostRequest)
    }
}