package com.fightpandemics.createpost.data

import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.result.Result
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePostRepositoryImpl @Inject constructor(private val postRemoteDataSource: PostRemoteDataSource) : CreatePostRepository {

    override suspend fun createPost(post: Post): Any {
        return flow {
            val result = postRemoteDataSource.createPost(post)
            emit(Result.Success(result))
        }
    }
}