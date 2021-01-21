package com.fightpandemics.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fightpandemics.profile.R
import kotlinx.android.synthetic.main.activity_social_media_links.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class SocialMediaLinksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_media_links)

        toolbar_title.text = getString(R.string.social_media_links)
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}