package com.fightpandemics.user

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fightpandemics.R
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class UserAvatar : RelativeLayout {
    private var mInflater: LayoutInflater

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        init("Null NAme", "")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
    }

    fun init(userNameInitials: String, userAvatarURL: String) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userImg = v.findViewById(R.id.customViewImg) as ImageView
        val userInitials = v.findViewById(R.id.textView1) as TextView
        val bitmapReturned = ImageLoadTask(userAvatarURL, userImg).execute().get()
        if (bitmapReturned != null) {
            userInitials.visibility = View.GONE
            userImg.setImageBitmap(bitmapReturned)
        } else {
            userImg.visibility = View.GONE
            userInitials.text = userNameInitials.toUpperCase(Locale.getDefault())
        }
    }

    fun init(userNameInitials: String) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userInitials = v.findViewById(R.id.textView1) as TextView
        val userImg = v.findViewById(R.id.customViewImg) as ImageView
        userImg.visibility = View.GONE
        userInitials.text = userNameInitials
    }
}
