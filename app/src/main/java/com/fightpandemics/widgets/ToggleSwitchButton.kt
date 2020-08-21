package com.fightpandemics.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.fightpandemics.R


@SuppressLint("ViewConstructor")
@Suppress("LongParameterList")
class ToggleSwitchButton(
    context: Context,
    private var position: Int,
    private var positionType: PositionType,
    listener: Listener,
    layoutId: Int,
    prepareDecorator: ToggleSwitchButtonDecorator,
    private var checkedDecorator: ViewDecorator?,
    private var checkedBackgroundColor: Int,
    private var uncheckedDecorator: ViewDecorator?,
    private var checkedBorderColor: Int,
    private var borderRadius: Float,
    private var borderWidth: Float,
    private var uncheckedBackgroundColor: Int,
    private var uncheckedBorderColor: Int,
    separatorColor: Int,
    private var toggleMargin: Int
) :
        LinearLayout(context), IRightToLeftProvider {

    interface ToggleSwitchButtonDecorator {
        fun decorate(toggleSwitchButton: ToggleSwitchButton, view: View, position: Int)
    }

    interface ViewDecorator {
        fun decorate(view: View, position: Int)
    }

    interface Listener {
        fun onToggleSwitchClicked(button: ToggleSwitchButton)
    }

    enum class PositionType {
        LEFT, CENTER, RIGHT
    }

    private var toggleWidth = 0
    private var toggleHeight = LayoutParams.MATCH_PARENT
    var isChecked = false
    private var rightToLeftProvider: IRightToLeftProvider = this

    private var separator: View
    private var view: View

    init {

        // Inflate Layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.toggle_switch_button, this, true) as LinearLayout
        val relativeLayoutContainer = layoutView.findViewById(R.id.relative_layout_container) as RelativeLayout

        // View
        val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)

        view  = LayoutInflater.from(context).inflate(layoutId, relativeLayoutContainer, false)
        relativeLayoutContainer.addView(view, params)

        // Bind Views
        separator = layoutView.findViewById(R.id.separator)
        val clickableWrapper: LinearLayout = findViewById(R.id.clickable_wrapper)

        // Setup View
        val layoutParams = LayoutParams(toggleWidth, toggleHeight, 1.0f)

        if (toggleMargin > 0 && !isFirst()) {
            layoutParams.setMargins(toggleMargin, 0, 0, 0)
        }

        this.layoutParams = layoutParams

        this.orientation = HORIZONTAL
        this.background = getBackgroundDrawable(uncheckedBackgroundColor, uncheckedBorderColor)

        separator.setBackgroundColor(separatorColor)

        clickableWrapper.setOnClickListener {
            listener.onToggleSwitchClicked(this)
        }

        // Decorate
        prepareDecorator.decorate(this, view, position)
        uncheckedDecorator?.decorate(view, position)
    }

    // Public instance methods

    fun check() {
        this.background = getBackgroundDrawable(checkedBackgroundColor, checkedBorderColor)
        this.isChecked = true
        checkedDecorator?.decorate(view, position)
    }

    fun uncheck() {
        this.background = getBackgroundDrawable(uncheckedBackgroundColor, uncheckedBorderColor)
        this.isChecked = false
        uncheckedDecorator?.decorate(view, position)
    }

    fun setSeparatorVisibility(isSeparatorVisible : Boolean) {
        separator.visibility = if (isSeparatorVisible) View.VISIBLE else View.GONE
    }

    // Private instance methods

    private fun getBackgroundDrawable(backgroundColor : Int, borderColor : Int) : GradientDrawable {

        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(backgroundColor)

        gradientDrawable.cornerRadii = getCornerRadii()

        if (borderWidth > 0) {
            gradientDrawable.setStroke(borderWidth.toInt(), borderColor)
        }

        return gradientDrawable
    }

    companion object {
        private const val CORNER_RADIUS_SIZE = 8
    }

    private fun getCornerRadii(): FloatArray {
        return if (toggleMargin > 0) {
            FloatArray(CORNER_RADIUS_SIZE) { borderRadius}
        } else {
            val isRightToLeft = rightToLeftProvider.isRightToLeft()
            when(positionType) {
                PositionType.LEFT ->
                    if (isRightToLeft) getRightCornerRadii() else getLeftCornerRadii()

                PositionType.RIGHT ->
                    if (isRightToLeft) getLeftCornerRadii() else getRightCornerRadii()

                else -> FloatArray(CORNER_RADIUS_SIZE) { 0F }
            }
        }
    }

    override fun isRightToLeft(): Boolean {
        return false
    }

    private fun getRightCornerRadii(): FloatArray {
        return floatArrayOf(0f, 0f, borderRadius, borderRadius, borderRadius, borderRadius, 0f, 0f)
    }

    private fun getLeftCornerRadii(): FloatArray {
        return floatArrayOf(borderRadius, borderRadius, 0f, 0f, 0f, 0f, borderRadius, borderRadius)
    }

    private fun isFirst() : Boolean {
        return if (rightToLeftProvider.isRightToLeft()) {
            positionType == PositionType.RIGHT
        } else {
            positionType == PositionType.LEFT
        }
    }
}
