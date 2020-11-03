package com.fightpandemics.core.data.remote.posts

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.post.PostRequest
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.data.model.posts.Posts
import timber.log.Timber
import javax.inject.Inject

class PostsRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI,
) : PostsRemoteDataSource {

    override suspend fun fetchPosts(): Posts =
        fightPandemicsAPI.getPosts()

    override suspend fun fetchPosts(objective: String?): List<Post> =
        fightPandemicsAPI.getPosts(objective)

    override suspend fun updatePost(postId: String, postRequest: PostRequest) {
        val d = fightPandemicsAPI.updatePost(postId, postRequest)
        Timber.e(d.isSuccessful.toString())
    }

    override suspend fun likePost(postId: String, userId: String, like: Boolean) {
        when {
            like -> fightPandemicsAPI.likePost(postId, userId)
            else -> fightPandemicsAPI.unlikePost(postId, userId)
        }
    }
}