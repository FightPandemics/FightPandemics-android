package com.fightpandemics.home.ui.tabs.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.R

class HomeRequestAdapter(private val posts: List<Post>) :
    RecyclerView.Adapter<HomeRequestViewHolder>() {

    var onItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRequestViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_feed, parent, false)
        return HomeRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(postsViewHolder: HomeRequestViewHolder, position: Int) {
        postsViewHolder.bind(posts.get(position), onItemClickListener)
    }

    override fun getItemCount(): Int = posts.size
}