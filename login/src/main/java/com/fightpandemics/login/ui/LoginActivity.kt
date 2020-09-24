package com.fightpandemics.login.ui

import com.fightpandemics.ui.BaseActivity

class LoginActivity : BaseActivity() {

    override fun onResume() {
        super.onResume()
        replaceFragment(SignUpFragment.newInstance("teste", "teste"), false)
    }
}