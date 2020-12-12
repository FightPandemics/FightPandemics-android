package com.fightpandemics.search.utils

import android.graphics.Color
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.fightpandemics.search.R

val TAB_TITLES = arrayOf(
    R.string.tab_posts,
    R.string.tab_organizations,
    R.string.tab_people
)


fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
    if (tv.tag == null) {
        tv.tag = tv.text
    }
    val vto = tv.viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val obs = tv.viewTreeObserver
            obs.removeGlobalOnLayoutListener(this)
            if (maxLine == 0) {
                val lineEndIndex = tv.layout.getLineEnd(0)
                val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                    .toString() + " " + expandText
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                        viewMore
                    ), TextView.BufferType.SPANNABLE
                )
            } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                    .toString() + " " + expandText
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                        viewMore
                    ), TextView.BufferType.SPANNABLE
                )
            } else {
                val lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                val text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                        viewMore
                    ), TextView.BufferType.SPANNABLE
                )
            }
        }
    })
}


private fun addClickablePartTextViewResizable(
    strSpanned: Spanned, tv: TextView,
    maxLine: Int, spanableText: String, viewMore: Boolean
): SpannableStringBuilder? {
    val str = strSpanned.toString()
    val ssb = SpannableStringBuilder(strSpanned)
    if (str.contains(spanableText)) {
        ssb.setSpan(object : MySpannable(false) {
            override fun onClick(widget: View) {
                if (viewMore) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                    tv.invalidate()
                    makeTextViewResizable(tv, -1, "View Less", false)
                } else {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                    tv.invalidate()
                    makeTextViewResizable(tv, 3, "View More", true)
                }
            }
        }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
    }
    return ssb
}

open class MySpannable(isUnderline: Boolean) : ClickableSpan() {
    private var isUnderline = false
    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.color = Color.parseColor("#343434")
    }

    override fun onClick(widget: View) {}

    /**
     * Constructor
     */
    init {
        this.isUnderline = isUnderline
    }
}
