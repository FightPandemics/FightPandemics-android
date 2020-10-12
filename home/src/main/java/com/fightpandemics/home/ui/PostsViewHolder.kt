package com.fightpandemics.home.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.R
import com.fightpandemics.utils.GlideApp
import com.fightpandemics.widgets.ProfileImageView

class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val user_full_name = itemView.findViewById(R.id.user_full_name) as TextView
    val post_title = itemView.findViewById(R.id.post_title) as TextView
    val user_location = itemView.findViewById(R.id.user_location) as TextView
    val post_content = itemView.findViewById(R.id.post_content) as TextView
    val user_avatar = itemView.findViewById(R.id.user_avatar) as ProfileImageView

    fun bind(post: Post, onItemClickListener: ((Post) -> Unit)?) = with(itemView) {

        GlideApp.with(this)
            .load(post.author?.photo)
            .centerCrop()
            .apply(RequestOptions().override(layoutParams.width, layoutParams.height))
            .into(user_avatar)

        user_full_name.text = post.author?.name
        post_title.text = post.title
        //user_location.text = post.author?.location.toString()

        post_content.text = post.content

//        pokeNumber.text = pokemon.id.prettyPrintId()
//        pokeName.text = pokemon.identifier.capitalize()


        setOnClickListener { onItemClickListener?.invoke(post) }

    }
}