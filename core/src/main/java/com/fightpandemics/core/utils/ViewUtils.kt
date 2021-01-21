package com.fightpandemics.core.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun showInputMethod(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun hideInputMethod(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}