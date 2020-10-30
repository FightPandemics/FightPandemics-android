package com.fightpandemics.login.dagger

import com.fightpandemics.login.ui.CompleteProfileFragment
import com.fightpandemics.login.ui.SignInEmailFragment
import com.fightpandemics.login.ui.SignInFragment
import com.fightpandemics.login.ui.SignUpFragment

fun inject(fragment: SignUpFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}

fun inject(fragment: SignInFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}

fun inject(fragment: SignInEmailFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}

fun inject(fragment: CompleteProfileFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}