package com.fightpandemics.createpost.domain.repository

import com.fightpandemics.core.result.Result
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePostRepositoryImpl @Inject constructor(private val postRemoteDataSource: PostRemoteDataSource) : CreatePostRepository {

    override suspend fun createPost(createPostRequest: CreatePostRequest): Any {
        return flow {
            val result = postRemoteDataSource.createPost(createPostRequest)
            emit(Result.Success(result))
        }
    }
}