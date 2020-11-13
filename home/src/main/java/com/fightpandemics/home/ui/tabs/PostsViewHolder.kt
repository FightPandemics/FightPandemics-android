package com.fightpandemics.home.ui.tabs

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.ItemAllFeedBinding
import com.fightpandemics.home.databinding.SingleChipLayoutBinding
import com.fightpandemics.home.ui.HomeEventListener
import com.fightpandemics.home.ui.HomeOptionsBottomSheetFragment
import com.fightpandemics.home.utils.userInitials
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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


            // Options for User to Edit or Delete his post.
            when (post.author!!.id) {
                homeEventListener.userId() -> {
                    itemBinding.postOption.isVisible = true

                    val fragmentManager = (context as FragmentActivity).supportFragmentManager
                    val homeOptionsBottomSheetFragment =
                        HomeOptionsBottomSheetFragment.newInstance()

                    itemBinding.postOption.setOnClickListener {
                        homeOptionsBottomSheetFragment
                            .show(fragmentManager, homeOptionsBottomSheetFragment.tag)

                        // execute the commited transaction before trying to access the view
                        fragmentManager.executePendingTransactions()

                        // accessing button view
                        homeOptionsBottomSheetFragment
                            .view?.findViewById<MaterialButton>(R.id.btn_edit_post)
                            ?.setOnClickListener {
                                Timber.e("EDIT ${post.author}")
                                // TODO - Launch Create Post Screen filled with elements from this post.
                                homeOptionsBottomSheetFragment.dismissAllowingStateLoss()
                            }

                        homeOptionsBottomSheetFragment
                            .view?.findViewById<MaterialButton>(R.id.btn_delete_post)
                            ?.setOnClickListener {

                                MaterialAlertDialogBuilder(this.context, R.style.PostMaterialDialog)
                                    .setTitle(R.string.delete_confirm_alert_title)
                                    .setMessage(R.string.delete_confirm_alert_msg)
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Delete") { dialog, which ->
                                        Timber.e("DELETE ${post.author}")

                                        homeEventListener.onDeleteClicked(post).apply {

                                        }

                                        // TODO - This Toast in the return of the delete
                                        val layoutInflater = LayoutInflater.from(context)
                                        val customLayout = layoutInflater.inflate(
                                            R.layout.delete_post_feedback,
                                            findViewById(R.id.delete_post_feedback)
                                        )
                                        with(Toast(this.context.applicationContext)){
                                            duration = Toast.LENGTH_SHORT
                                            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                                            view = customLayout
                                            show()
                                        }
                                    }
                                    .show()

                                homeOptionsBottomSheetFragment.dismissAllowingStateLoss()
                            }
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

