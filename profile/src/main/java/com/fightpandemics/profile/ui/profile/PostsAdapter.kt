package com.fightpandemics.profile.ui.profile

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.profile.R
import com.fightpandemics.profile.databinding.ItemUserPostsBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import okhttp3.internal.notify
import timber.log.Timber

class PostsAdapter: RecyclerView.Adapter<PostViewHolder>() {

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

        holder.bind(item) { post -> Timber.i("hello") }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}