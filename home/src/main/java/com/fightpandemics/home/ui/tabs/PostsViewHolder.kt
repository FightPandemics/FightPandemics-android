package com.fightpandemics.home.ui.tabs

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.ItemAllFeedBinding
import com.fightpandemics.home.databinding.SingleChipLayoutBinding
import com.fightpandemics.home.ui.HomeEventListener
import com.fightpandemics.home.ui.HomeFragmentDirections
import com.fightpandemics.home.utils.getPostCreated
import com.fightpandemics.home.utils.sharePost
import com.fightpandemics.home.utils.userInitials
import timber.log.Timber
import java.util.*

class PostsViewHolder(
    private val homeEventListener: HomeEventListener,
    private var itemBinding: ItemAllFeedBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    @SuppressLint("RestrictedApi")
    fun bind(post: Post, onItemClickListener: ((Post) -> Unit)?) {
        with(itemBinding.root) {

            // Display author avatar
            if (post.author?.photo != null) {
                GlideApp.with(this)
                    .load(post.author?.photo)
                    .centerCrop()
                    .apply(RequestOptions().override(layoutParams.width, layoutParams.height))
                    .into(itemBinding.userAvatar)
            } else {
                itemBinding.userAvatar.setInitials(userInitials(post.author?.name))
                itemBinding.userAvatar.setBorderColor(
                    ContextCompat.getColor(
                        this.context,
                        R.color.colorPrimary
                    )
                )
            }

            //
            itemBinding.objective.text = post.objective?.capitalize(Locale.ROOT)
            itemBinding.userFullName.text = post.author?.name
            itemBinding.postTitle.text = post.title
            itemBinding.userLocation.text =
                post.author?.location?.city.plus(", ").plus(post.author?.location?.country)

            itemBinding.postContent.text = post.content

            itemBinding.like.apply {
                isChecked = post.liked!!
                setOnClickListener {
                    post.liked = !post.liked!!
                    homeEventListener.onLikeClicked(post)
                }
            }

            // Display Post like counts
            itemBinding.likesCount.apply {
                text = post.likesCount.toString()
            }

            // Display Post comment counts.
            itemBinding.commentsCount.text = post.commentsCount.toString()

            // Share a Post
            itemBinding.share.setOnClickListener {
                context.startActivity(sharePost(post.title, post._id))
            }

            // Display Post tags/types.
            itemBinding.chipGroup.removeAllViews()
            for (type: String in post.types!!) {
                val singleChipLayoutBinding = SingleChipLayoutBinding.inflate(
                    LayoutInflater.from(this.context),
                    itemBinding.chipGroup,
                    false
                )
                singleChipLayoutBinding.chip.text = type
                singleChipLayoutBinding.chip.isEnabled = false
                itemBinding.chipGroup.addView(singleChipLayoutBinding.chip)
            }

            // Display Post options to Edit or Delete his/her post.
            when (post.author!!.id) {
                homeEventListener.userId() -> {
                    itemBinding.postOption.isVisible = true
                    itemBinding.postOption.setOnClickListener {
                        findNavController().navigate(
                            HomeFragmentDirections
                                .actionHomeFragmentToHomeOptionsBottomSheetFragment(post)
                        )
                    }
                }
                else -> itemBinding.postOption.isVisible = false
            }

            // Display time of Post
            "Posted ${getPostCreated(post.createdAt.toString())} ago".also {
                itemBinding.timePosted.text = it
            }

            Timber.e(getPostCreated(post.createdAt.toString()))
            Timber.e(getPostCreated(post.updatedAt.toString())?.plus("EDITED"))

            setOnClickListener { onItemClickListener?.invoke(post) }
        }
    }
}
