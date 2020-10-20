package com.fightpandemics.home.ui.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.home.R

class PostsAdapter : RecyclerView.Adapter<PostsViewHolder>() {

    private val mPostsDiffer: AsyncListDiffer<Post> =
        AsyncListDiffer(this, POSTS_DIFF_CALLBACK)

    var onItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_feed, parent, false)
        return PostsViewHolder(itemView)
    }

    override fun onBindViewHolder(postsViewHolder: PostsViewHolder, position: Int) {
        val post: Post = mPostsDiffer.currentList.get(position)
        postsViewHolder.bind(post, onItemClickListener)
    }

    override fun getItemCount(): Int = mPostsDiffer.currentList.size

    fun submitList(posts: List<Post>) {
        mPostsDiffer.submitList(posts)
    }

    companion object {
        val POSTS_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldPost: Post, newPost: Post): Boolean {
                return oldPost.author?.id === newPost.author?.id
            }

            override fun areContentsTheSame(oldPost: Post, newPost: Post): Boolean {
                return oldPost == newPost
            }
        }
    }
}

