package com.fightpandemics.core.data.remote.posts

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PostsRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI,
) : PostsRemoteDataSource {

    override suspend fun fetchPosts(): Posts =
        fightPandemicsAPI.getPosts()

    override suspend fun fetchPosts(objective: String?): Response<List<Post>> =
        fightPandemicsAPI.getPosts(objective, 20)

    override suspend fun fetchPostsByAuthor(
        authorId: String
    ): List<Post> =
        fightPandemicsAPI.getPostsByAuthor(true, 10, 0, authorId)

    override suspend fun updatePost(postId: String, postRequest: PostRequest) {
        val d = fightPandemicsAPI.updatePost(postId, postRequest)
        Timber.e(d.isSuccessful.toString())
    }

    override suspend fun deletePost(postId: String) {
        val e = fightPandemicsAPI.deletePost(postId)
    }

    override suspend fun likePost(postId: String, userId: String, like: Boolean) {
        when {
            like -> fightPandemicsAPI.likePost(postId, userId)
            else -> fightPandemicsAPI.unlikePost(postId, userId)
        }
    }
}
