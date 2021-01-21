package com.fightpandemics.home.ui

import com.fightpandemics.core.data.model.posts.Post

/**
 * Actions that can be performed on events.
 */
interface EventActions {
    fun onLikeClicked(post: Post)
    fun onEditClicked(post: Post)
    fun onDeleteClicked(post: Post)
}
