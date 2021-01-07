package com.fightpandemics.login.ui

import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment  : Fragment() {

    fun displayorHideView(visibility: Int, view : View) {
        view.apply {this.visibility = visibility}
    }

}