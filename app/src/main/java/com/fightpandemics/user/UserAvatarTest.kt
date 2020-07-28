package com.fightpandemics.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fightpandemics.R

class UserAvatarTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_test)

        val ua = findViewById<UserAvatar>(R.id.userAvatar)
        ua.init("RG", "https://i.ytimg.com/vi/Tdac7EAyL80/maxresdefault.jpg")
    }
}