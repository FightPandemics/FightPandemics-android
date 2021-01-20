package com.fightpandemics.login.util

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.fightpandemics.login.R
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

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
        view.setBackgroundColor(
            ContextCompat.getColor(
                this@snack.context,
                R.color.fightPandemicsNeonBlue
            )
        )
        actionCallBack?.let {
            actionText?.let {
                setAction(actionText) {
                    actionCallBack()
                }
            }
        }
    }.show()
}

fun showKeyboard(view: View) {
    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun dismissKeyboard(view: View) {
    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextView.joinNow(view: View) {
    val joinString =
        SpannableString(view.context.getString(R.string.don_t_have_an_account_join_now_instead))

    val joinNowClickableSpan: ClickableSpan = object : JoinNowClickableSpan() {
        override fun onClick(widget: View) {
            widget.invalidate()
            findNavController().navigate(R.id.action_signInEmailFragment_to_signUpFragment)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    joinString.setSpan(joinNowClickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    joinString.setSpan(UnderlineSpan(), 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    setText(joinString, TextView.BufferType.SPANNABLE)
    setMovementMethod(LinkMovementMethod.getInstance())
}

fun allInputFieldsHaveBeenFilled(email: String?, password: String?): Boolean {
    if (email == "" || password == "") {
        return false
    }
    return true
}

fun inValidEmail(value: String?): String? {
    val valueMatcher = Pattern
        .compile(Patterns.EMAIL_ADDRESS.pattern())
        .matcher(value as CharSequence)
    if (valueMatcher.matches()) return null
    return "Please Enter a Valid Email Address"
}
