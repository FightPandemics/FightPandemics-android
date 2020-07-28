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


class UserAvatar : RelativeLayout {
    private var mInflater: LayoutInflater

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        init("NN")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
    }

    fun init(userNameInitials: String, userAvatarURL: String, context: Context) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userImg = v.findViewById(R.id.customViewImg) as ImageView
        val userInitials = v.findViewById(R.id.textView1) as TextView
        val request = ImageRequest(userAvatarURL,
            Response.Listener<Bitmap?> { b -> userImg.setImageBitmap(b) }, 0, 0, null,
            Response.ErrorListener {
                userImg.visibility = View.GONE
                userInitials.text = userNameInitials
            })

        val rQueue = Volley.newRequestQueue(context)
        rQueue.add(request)
    }

    fun init(userNameInitials: String) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val userInitials = v.findViewById(R.id.textView1) as TextView
        val userImg = v.findViewById(R.id.customViewImg) as ImageView
        userImg.visibility = View.GONE
        userInitials.text = userNameInitials
    }
}
