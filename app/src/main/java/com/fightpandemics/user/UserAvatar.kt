package com.fightpandemics.user

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.fightpandemics.R
import java.util.*
import kotlin.properties.Delegates

class UserAvatar : RelativeLayout {
    private var mInflater: LayoutInflater

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        init("Null NAme")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
    }

    fun init(name: String) {
        val v: View = mInflater.inflate(R.layout.user_avatar_view, this, true)
        val tv = v.findViewById(R.id.textView1) as TextView
        val firstInitial = name.substring(0, 1).toUpperCase(Locale.getDefault())
        var index by Delegates.notNull<Int>()
        lateinit var lastInitial: CharSequence
        lateinit var finalText: String
        if (name.contains(" ")) {
            index = name.indexOf(" ")
            lastInitial = name.substring(index + 1, index + 2).toUpperCase(Locale.getDefault())
            finalText = "$firstInitial$lastInitial"
        } else {
            finalText = firstInitial
        }
        tv.text = finalText

    }
}