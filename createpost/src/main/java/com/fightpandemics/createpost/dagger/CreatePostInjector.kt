package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.ui.CreatePostFragment

/*
* created by Osaigbovo Odiase
* */
fun inject(fragment: CreatePostFragment) {
    (fragment.requireActivity().applicationContext as CreatePostComponentProvider)
        .provideCreatePostComponent()
        .inject(fragment)
}
