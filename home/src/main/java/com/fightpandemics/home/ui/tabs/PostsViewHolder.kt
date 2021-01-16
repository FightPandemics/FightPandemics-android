package com.fightpandemics.home.ui.tabs

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.ItemAllFeedBinding
import com.fightpandemics.home.databinding.SingleChipLayoutBinding
import com.fightpandemics.home.ui.HomeEventListener
import com.fightpandemics.home.utils.userInitials
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber
import java.util.*


class PostsViewHolder(
    private val homeEventListener: HomeEventListener,
    private var itemBinding: ItemAllFeedBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    @SuppressLint("RestrictedApi")
    fun bind(post: Post, onItemClickListener: ((Post) -> Unit)?) {
        with(itemBinding.root) {

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

            itemBinding.likesCount.apply {
                text = post.likesCount.toString()
            }



            itemBinding.commentsCount.text = post.commentsCount.toString()



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



            when (post.author!!.id) {
                homeEventListener.userId() -> {
                    itemBinding.postOption.isVisible = true
                    itemBinding.postOption.setOnClickListener {
                        val view: View = View.inflate(context, R.layout.edit_delete, null)
                        val mBottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
                        mBottomSheetDialog.setContentView(view)
                        mBottomSheetDialog.show()
                    }
                }
                else -> {
                    itemBinding.postOption.isVisible = false
                }
            }

            val time_post = 12.toString()
            itemBinding.timePosted.text = "Posted $time_post hrs ago"
            //Timber.e(getPostCreated("2020-10-15T15:44:04.009Z").toString())

            setOnClickListener { onItemClickListener?.invoke(post) }
        }
    }


}

