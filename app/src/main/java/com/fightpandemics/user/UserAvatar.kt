package com.fightpandemics.user

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.fightpandemics.R
import java.lang.RuntimeException

class UserAvatar : RelativeLayout {
    private var mInflater: LayoutInflater
    private var imageUrl : String
    private var userInitials : String

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        imageUrl = ""
        userInitials = "NN"
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
        imageUrl = ""
        userInitials = ""
        if(attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.UserAvatar, 0, 0)
            try {
                this.imageUrl = attributes.getString(R.styleable.UserAvatar_imageUrl).toString()
                this.userInitials = attributes.getString(R.styleable.UserAvatar_userInitials).toString()
            } finally {
                attributes.recycle()
            }
        } else {
            throw RuntimeException("AttributeSet is null!")
        }
    }

    fun setUpUserAvatar(context: Context) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userImg = v.findViewById(R.id.userAvatarImage) as ImageView
        val userInitials = v.findViewById(R.id.userAvatarInitials) as TextView

        val imageRequest = ImageRequest(this.imageUrl, Response.Listener {
            userImg.setImageBitmap(it)
        }, userImg.layoutParams.width, userImg.layoutParams.height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
            Response.ErrorListener {
                userImg.visibility = View.GONE
                userInitials.text = this.userInitials
            })

        val rQueue = Volley.newRequestQueue(context)
        rQueue.add(imageRequest)
    }

    fun setUpUserAvatar(userNameInitials: String, userAvatarURL: String, context: Context) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userImg = v.findViewById(R.id.userAvatarImage) as ImageView
        val userInitials = v.findViewById(R.id.userAvatarInitials) as TextView

        val imageRequest = ImageRequest(userAvatarURL, Response.Listener {
            userImg.setImageBitmap(it)
        }, userImg.layoutParams.width, userImg.layoutParams.height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
            Response.ErrorListener {
                userImg.visibility = View.GONE
                userInitials.text = userNameInitials
            })

        val rQueue = Volley.newRequestQueue(context)
        rQueue.add(imageRequest)
    }

    fun setUpUserAvatar(userNameInitials: String) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userInitials = v.findViewById(R.id.userAvatarInitials) as TextView
        val userImg = v.findViewById(R.id.userAvatarImage) as ImageView
        userImg.visibility = View.GONE
        userInitials.text = userNameInitials
    }
}
