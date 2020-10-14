package com.fightpandemics.home.ui.tabs

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.fightpandemics.data.model.posts.Post
import com.fightpandemics.home.R
import com.fightpandemics.home.utils.userInitials
import com.fightpandemics.utils.GlideApp
import com.fightpandemics.widgets.ProfileImageView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import java.util.*

class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val user_objective = itemView.findViewById(R.id.objective) as TextView
    val user_avatar = itemView.findViewById(R.id.user_avatar) as ProfileImageView
    val user_full_name = itemView.findViewById(R.id.user_full_name) as TextView
    val post_title = itemView.findViewById(R.id.post_title) as TextView
    val user_location = itemView.findViewById(R.id.user_location) as TextView
    val post_content = itemView.findViewById(R.id.post_content) as TextView
    val likes_count = itemView.findViewById(R.id.likes_count) as TextView
    val comments_count = itemView.findViewById(R.id.comments_count) as TextView
    val chipGroup = itemView.findViewById(R.id.chipGroup) as ChipGroup

    fun bind(post: Post, onItemClickListener: ((Post) -> Unit)?) = with(itemView) {

        if (post.author?.photo != null) {
            GlideApp.with(this)
                .load(post.author?.photo)
                .centerCrop()
                .apply(RequestOptions().override(layoutParams.width, layoutParams.height))
                .into(user_avatar)
        } else {
            user_avatar.setInitials(userInitials(post.author?.name))
            user_avatar.setBorderColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
        }

        user_objective.text = post.objective?.capitalize(Locale.ROOT)
        user_full_name.text = post.author?.name
        post_title.text = post.title
        user_location.text =
            post.author?.location?.state.plus(", ").plus(post.author?.location?.country)
        post_content.text = post.content
        likes_count.text = post.likesCount.toString()
        comments_count.text = post.commentsCount.toString()

        for (type: String in post.types!!) {
            val chip = LayoutInflater.from(this.context)
                .inflate(R.layout.single_chip_layout, chipGroup, false) as Chip
            chip.text = type
            chip.isEnabled = false
            chipGroup.addView(chip)
        }

        Timber.e(post.expireAt.toString())

        setOnClickListener { onItemClickListener?.invoke(post) }

    }
}

