package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.post.CreatePostRequest
import com.fightpandemics.core.data.model.post.CreatePostResponse
import com.fightpandemics.core.data.model.post.ErrorResponse
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.parseErrorJsonResponse
import com.fightpandemics.core.utils.parseJsonResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PostsRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val preferenceStorage: PreferenceStorage,
    private val postsRemoteDataSource: PostsRemoteDataSource,
) : PostsRepository {

    override fun getPosts(): Flow<Result<Posts>> {
        return flow {
            val posts = postsRemoteDataSource.fetchPosts()
            emit(Result.Success(posts))
        }
    }

    // TODO - Save in database
    override suspend fun getPosts(objective: String?): Flow<Result<*>> {
        return flow<Result<*>> {
            val posts = postsRemoteDataSource.fetchPosts(objective).body()!!
            // .onEach { save(it) } //save to database, fetch from database and emit(dao.fetch)
            emit(Result.Success(posts))
//            delay(5000)
//            getPosts(objective)
        }.catch { cause ->
            val error = postsRemoteDataSource.fetchPosts(objective).errorBody().toString()
            emit(Result.Error(Exception(error)))
        }
    }

    fun save(posts: List<Post>) {
    }

    override suspend fun getPostsByAuthor(
        authorId: String
    ): Flow<Result<List<Post>>> {
        return flow {
            val posts = postsRemoteDataSource.fetchPostsByAuthor(authorId)
            emit(Result.Success(posts))
        }
    }

    override suspend fun editPost(postRequest: PostRequest) {
        // postsRemoteDataSource.updatePost(postRequest._id, postRequest)
    }

    override suspend fun deletePost(post: Post): Flow<Result<*>> {
        return flow<Result<*>> {
            val response = postsRemoteDataSource.deletePost(post._id)
            emit(Result.Success(response))
        }.catch { cause ->
            emit(Result.Error(Exception(cause)))
        }
    }

    override suspend fun likePost(post: Post) {
        val userId = preferenceStorage.userId
        postsRemoteDataSource.likePost(post._id, userId!!, post.liked!!)
    }

    @ExperimentalCoroutinesApi
    override suspend fun createPost(createPostRequest: CreatePostRequest): Flow<Result<*>> {
        return channelFlow {
            val response = postsRemoteDataSource.createPost(createPostRequest)
            if (response.isSuccessful && response.code() == 201) {
                val postResponse = response.parseJsonResponse<CreatePostResponse>(moshi)
                offer(Result.Success(postResponse))
            }

            if (response.code() != 200 && response.code() != 201) {
                val errorReturned = response.parseErrorJsonResponse<ErrorResponse>(moshi)
                offer(Result.Error(Exception(errorReturned!!.message)))
            }
            awaitClose { }
        }
    }
}
