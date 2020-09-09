package com.fightpandemics.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fightpandemics.R
import com.fightpandemics.utils.GlideApp

class UserAvatar : RelativeLayout {
    private var mInflater: LayoutInflater
    private var imageUrl: String
    private var userInitials: String

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        imageUrl = ""
        userInitials = "NN"
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
        imageUrl = ""
        userInitials = ""
        if (attrs != null) {
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.UserAvatar, 0, 0)
            try {
                this.imageUrl = attributes
                    .getString(R.styleable.UserAvatar_imageUrl).toString()
                this.userInitials = attributes
                    .getString(R.styleable.UserAvatar_userInitials).toString()
            } finally {
                attributes.recycle()
            }
        } else {
            throw NullPointerException("AttributeSet is null!")
        }
    }

    fun setUpUserAvatar(userNameInitials: String = "NN", userAvatarURL: String = "") {
        val view: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userImg = view.findViewById(R.id.userAvatarImage) as ImageView
        val userInitials = view.findViewById(R.id.userAvatarInitials) as TextView

        GlideApp
            .with(this.context)
            .load("userAvatarURL")
            .centerCrop()
            .apply(RequestOptions().override(layoutParams.width, layoutParams.height))
            .format(DecodeFormat.PREFER_ARGB_8888)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    userImg.visibility = View.GONE
                    userInitials.text = userNameInitials
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(userImg)
    }
}
