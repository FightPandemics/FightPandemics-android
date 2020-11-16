package com.fightpandemics.login.ui


enum class LoginConnection(val provider: String, val isSignUP: Boolean) {
    GOOGLE_SIGNIN("google-oauth2", false),
    FACEBOOK_SIGNIN("facebook", false),
    LINKEDIN_SIGNIN("linkedin", false),
    GOOGLE_SIGNUP("google-oauth2", true),
    FACEBOOK_SIGNUP("facebook", true),
    LINKEDIN_SIGNUP("linkedin", true)
}