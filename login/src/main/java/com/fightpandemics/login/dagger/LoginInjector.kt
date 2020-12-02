package com.fightpandemics.login.dagger

import com.fightpandemics.login.ui.signin.SignInFragment
import com.fightpandemics.login.ui.profile.CompleteProfileFragment
import com.fightpandemics.login.ui.signin.SignInEmailFragment
import com.fightpandemics.login.ui.verify.VerifyEmailFragment
import com.fightpandemics.login.ui.signup.SignUpEmailFragment
import com.fightpandemics.login.ui.signup.SignUpFragment

fun inject(fragment: SignUpFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}

fun inject(fragment: SignUpEmailFragment) {
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
fun inject(fragment: VerifyEmailFragment) {
    (fragment.requireActivity().applicationContext as LoginComponentProvider)
        .provideLoginComponent()
        .inject(fragment)
}