package com.fightpandemics.utils

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * An action to be performed when a bottom sheet's state is changed.
 */
interface OnStateChangedAction {
    fun onStateChanged(sheet: View, newState: Int)
}

/**
 * A state change action that handles showing the fab when the sheet is hidden and hiding the fab
 * when the sheet is not hidden.
 */
class ShowHideFabStateAction(
    private val fab: FloatingActionButton
) : OnStateChangedAction {

    override fun onStateChanged(sheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            fab.show()
        } else {
            fab.hide()
        }
    }
}

/**
 * A state change action that sets a view's visibility depending on whether the sheet is hidden
 * or not.
 *
 * By default, the view will be hidden when the sheet is hidden and shown when the sheet is shown
 * (not hidden). If [reverse] is set to true, the view will be shown when the sheet is hidden and
 * hidden when the sheet is shown (not hidden).
 */
class VisibilityStateAction(
    private val view: View,
    private val reverse: Boolean = false
) : OnStateChangedAction {
    override fun onStateChanged(sheet: View, newState: Int) {
        val stateHiddenVisibility = if (!reverse) View.GONE else View.VISIBLE
        val stateDefaultVisibility = if (!reverse) View.VISIBLE else View.GONE
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> view.visibility = stateHiddenVisibility
            else -> view.visibility = stateDefaultVisibility
        }
    }
}