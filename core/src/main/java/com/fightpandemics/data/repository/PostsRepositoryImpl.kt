package com.fightpandemics.data.repository

import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.data.model.posts.Posts
import com.fightpandemics.data.remote.PostsRemoteDataSource
import com.fightpandemics.domain.repository.PostsRepository
import com.fightpandemics.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PostsRepositoryImpl @Inject constructor(
    private val postsRemoteDataSource: PostsRemoteDataSource,
) : PostsRepository {

    private val postsChannel = ConflatedBroadcastChannel<Result<List<Post>>>()

    override fun getPosts(): Flow<Result<Posts>> {
        return flow {
            val posts = postsRemoteDataSource.fetchPosts()
            emit(Result.Success(posts))
        }
    }

    override suspend fun getPosts(objective: String?): Flow<Result<List<Post>>> {
        val posts = postsRemoteDataSource.fetchPosts(objective)
        postsChannel.offer(Result.Success(posts))
        return postsChannel.asFlow()
    }
//    return flow {
//            val posts = postsRemoteDataSource.fetchPosts(objective)
//            emit(Result.Success(posts))
//        }
}