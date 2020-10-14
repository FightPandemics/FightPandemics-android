package com.fightpandemics.data.remote

import com.fightpandemics.data.api.FightPandemicsAPI
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.data.model.posts.Posts
import javax.inject.Inject

class PostsRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI,
) : PostsRemoteDataSource {

    override suspend fun fetchPosts(): Posts =
        fightPandemicsAPI.getPosts()

    override suspend fun fetchPosts(objective: String?): List<Post> =
        fightPandemicsAPI.getPosts(objective)
}