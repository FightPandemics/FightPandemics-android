package com.fightpandemics.login.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.snack(
    message: String?,
    length: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    actionCallBack: (() -> Unit)? = null
) {
    Snackbar.make(this, message.toString(), length).apply {
        actionCallBack?.let {
            actionText?.let {
                setAction(actionText) {
                    actionCallBack()
                }
            }
        }
    }.show()
}