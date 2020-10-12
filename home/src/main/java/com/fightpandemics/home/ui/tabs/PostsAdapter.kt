package com.fightpandemics.home.ui.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.R
import com.fightpandemics.home.ui.PostsViewHolder

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsViewHolder>() {

    var onItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_feed, parent, false)
        return PostsViewHolder(itemView)
    }

    override fun onBindViewHolder(postsViewHolder: PostsViewHolder, position: Int) {
        postsViewHolder.bind(posts.get(position), onItemClickListener)
    }

    override fun getItemCount(): Int = posts.size
}