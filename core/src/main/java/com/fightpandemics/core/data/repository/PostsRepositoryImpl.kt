package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PostsRepositoryImpl @Inject constructor(
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

    override suspend fun deletePost(post: Post) {
        postsRemoteDataSource.deletePost(post._id)
    }

    override suspend fun likePost(post: Post) {
        val userId = preferenceStorage.userId
        postsRemoteDataSource.likePost(post._id, userId!!, post.liked!!)
    }
}
