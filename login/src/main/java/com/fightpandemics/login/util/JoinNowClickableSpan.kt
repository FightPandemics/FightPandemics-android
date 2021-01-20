package com.fightpandemics.login.util

import android.text.NoCopySpan
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

open class JoinNowClickableSpan : ClickableSpan(), NoCopySpan {
    override fun onClick(widget: View) {
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
    }
}
