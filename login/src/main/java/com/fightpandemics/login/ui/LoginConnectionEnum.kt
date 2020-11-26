package com.fightpandemics.login.ui

enum class LoginConnection(val provider: String) {
    GOOGLE("google-oauth2"),
    FACEBOOK("facebook"),
    LINKEDIN("linkedin")
}
