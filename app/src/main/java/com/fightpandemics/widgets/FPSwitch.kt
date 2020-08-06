package com.fightpandemics.widgets

import android.content.Context
import android.util.AttributeSet

class FPSwitch(context: Context, attrs: AttributeSet?) : BaseToggleSwitch(context, attrs) {

    interface OnChangeListener {
        fun onToggleSwitchChanged(position: Int)
    }

    var checkedPosition = 0
    set(value) {
        field = value
        for ((index, toggleSwitchButton) in buttons.withIndex()) {
            if (value == index) {
                toggleSwitchButton.check()
            }
            else {
                toggleSwitchButton.uncheck()
            }
        }
        manageSeparatorVisibility()
    }

    private var onChangeListener : OnChangeListener? = null

    override fun onRedrawn() {
        val currentToggleSwitch = buttons[checkedPosition]
        currentToggleSwitch.check()
        currentToggleSwitch.isClickable = false
        manageSeparatorVisibility()
    }

    override fun onToggleSwitchClicked(button: ToggleSwitchButton) {

        if (!button.isChecked && isEnabled) {
            buttons[checkedPosition].uncheck()

            checkedPosition = buttons.indexOf(button)

            button.check()

            manageSeparatorVisibility()

            onChangeListener?.onToggleSwitchChanged(checkedPosition)
        }
    }
}
