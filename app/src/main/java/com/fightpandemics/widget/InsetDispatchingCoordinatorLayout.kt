package com.fightpandemics.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.coordinatorlayout.R
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * A fixed version of [CoordinatorLayout] which properly dispatches [WindowInsets] to all children,
 * regardless of whether a child consumes the insets.
 *
 * Without this, views (say at index 0) consuming the insets, result in no other views receiving
 * the insets.
 */
@SuppressLint("PrivateResource") // R.attr.coordinatorLayoutStyle is private for some reason
class InsetDispatchingCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.coordinatorLayoutStyle
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val dispatched = super.dispatchApplyWindowInsets(WindowInsets(insets))
        val applied = onApplyWindowInsets(WindowInsets(insets))

        if (dispatched.isConsumed) {
            return dispatched
        }
        if (applied.isConsumed) {
            return applied
        }
        return dispatched
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // Give child views fresh insets.
            child.dispatchApplyWindowInsets(WindowInsets(insets))
        }
        return insets
    }
}
