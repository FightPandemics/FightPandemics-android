package com.fightpandemics.profile.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.profile.databinding.ItemUserPostsBinding
import timber.log.Timber

class PostsAdapter : RecyclerView.Adapter<PostViewHolder>() {

    var data = listOf<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: ItemUserPostsBinding = ItemUserPostsBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item) { Timber.i("hello") }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
