package com.fightpandemics.login.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.ActivityScope
import java.util.regex.Pattern
import javax.inject.Inject

@ActivityScope
class LoginViewModel @Inject constructor() : ViewModel() {

    fun doLogin(email: String, password: String) {}

    fun allInputFieldsHaveBeenFilled(email: String, password: String): Boolean {
        if (email == "" || password == "") {
            return false
        }
        return true
    }

    fun inValidEmail(value: String): String? {
        val valueMatcher = Pattern.compile(Patterns.EMAIL_ADDRESS.pattern()).matcher(value)
        if (valueMatcher.matches()) {
            return null
        }
        return "Please Enter a Valid Email Address"
    }
}