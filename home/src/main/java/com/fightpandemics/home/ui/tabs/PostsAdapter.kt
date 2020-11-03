package com.fightpandemics.home.ui.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.home.R
import com.fightpandemics.home.ui.HomeEventListener

class PostsAdapter(
    private val homeEventListener: HomeEventListener
) : RecyclerView.Adapter<PostsViewHolder>() {

    private val mPostsDiffer: AsyncListDiffer<Post> =
        AsyncListDiffer(this, POSTS_DIFF_CALLBACK)

    var onItemClickListener: ((Post) -> Unit)? = null
    var onLikeClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_feed, parent, false)
        return PostsViewHolder(homeEventListener, itemView)
    }

    override fun onBindViewHolder(postsViewHolder: PostsViewHolder, position: Int) {
        val post: Post = mPostsDiffer.currentList.get(position)
        postsViewHolder.bind(post, onLikeClickListener/*, onItemClickListener*/)
    }

    override fun getItemCount(): Int = mPostsDiffer.currentList.size

    fun submitList(posts: List<Post>) {
        mPostsDiffer.submitList(posts)
    }

    companion object {
        val POSTS_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldPost: Post, newPost: Post): Boolean =
                oldPost._id === newPost._id

            override fun areContentsTheSame(oldPost: Post, newPost: Post): Boolean =
                oldPost == newPost
        }
    }
}

