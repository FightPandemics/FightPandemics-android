package com.fightpandemics.createpost.data.remote

import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.createpost.data.api.NetworkApi
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(private val networkApi: NetworkApi) : PostRemoteDataSource {

    override suspend fun createPost(post: Post): Any {
        return networkApi.createPost(post)
    }
}