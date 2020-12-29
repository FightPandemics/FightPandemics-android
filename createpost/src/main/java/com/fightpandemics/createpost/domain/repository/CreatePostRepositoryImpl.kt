package com.fightpandemics.createpost.domain.repository

import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.parseErrorJsonResponse
import com.fightpandemics.core.utils.parseJsonResponse
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.data.model.CreatePostResponse
import com.fightpandemics.createpost.data.model.ErrorResponse
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class CreatePostRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val postRemoteDataSource: PostRemoteDataSource) : CreatePostRepository {

    @ExperimentalCoroutinesApi
    override suspend fun createPost(createPostRequest: CreatePostRequest): Flow<Result<*>> {
        return channelFlow {
            val response = postRemoteDataSource.createPost(createPostRequest)
            if (response.isSuccessful && response.code() == 201) {
                val postResponse = response.parseJsonResponse<CreatePostResponse>(moshi)
                offer(Result.Success(postResponse))
            }

            if (response.code() != 200 && response.code() != 201) {
                val errorReturned = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                offer(Result.Error(Exception(errorReturned!!.message)))
            }
            awaitClose {  }
        }
    }
}