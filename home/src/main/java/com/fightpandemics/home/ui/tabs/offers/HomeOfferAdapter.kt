package com.fightpandemics.home.ui.tabs.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.R

class HomeOfferAdapter(private val posts: List<Post>) : RecyclerView.Adapter<HomeOfferViewHolder>() {

    var onItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOfferViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_feed, parent, false)
        return HomeOfferViewHolder(itemView)
    }

    override fun onBindViewHolder(postsViewHolder: HomeOfferViewHolder, position: Int) {
        postsViewHolder.bind(posts.get(position), onItemClickListener)
    }

    override fun getItemCount(): Int = posts.size
}