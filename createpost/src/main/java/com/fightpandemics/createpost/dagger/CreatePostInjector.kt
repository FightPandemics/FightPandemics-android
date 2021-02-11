package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.ui.CreatePostFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
fun inject(fragment: CreatePostFragment) {
    (fragment.requireActivity().applicationContext as CreatePostComponentProvider)
        .provideCreatePostComponent()
        .inject(fragment)
}
