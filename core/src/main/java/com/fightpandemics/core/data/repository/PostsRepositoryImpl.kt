package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.domain.repository.PostsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PostsRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
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
        /*val posts = postsRemoteDataSource.fetchPosts(objective)
        postsChannel.offer(Result.Success(posts))
        return postsChannel.asFlow()*/

        return flow {
            val posts = postsRemoteDataSource.fetchPosts(objective)
            emit(Result.Success(posts))
        }
    }

    override suspend fun updatePost(postRequest: PostRequest) {
        postsRemoteDataSource.updatePost(postRequest.post._id, postRequest)
    }

    override suspend fun updatePost(postId: String) {
        val userId = preferenceStorage.userId
        postsRemoteDataSource.updatePost(postId, userId!!)
    }
}