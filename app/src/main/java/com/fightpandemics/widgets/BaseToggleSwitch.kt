package com.fightpandemics.widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.fightpandemics.R

abstract class BaseToggleSwitch : LinearLayout, ToggleSwitchButton.Listener {

    // Default Values
    companion object Default {

        @JvmStatic private val BORDER_RADIUS_DP = 4
        @JvmStatic private val BORDER_WIDTH = 1

        @JvmStatic private val CHECKED_BACKGROUND_COLOR = R.color.fightPandemicsGhostWhite
        @JvmStatic private val CHECKED_BORDER_COLOR = R.color.fightPandemicsNeonBlue
        @JvmStatic private val CHECKED_TEXT_COLOR = R.color.fightPandemicsNeonBlue

//        @JvmStatic private val EMPTY_TOGGLE_DECORATOR = object: ToggleSwitchButton.ToggleSwitchButtonDecorator {
//            override fun decorate(toggleSwitchButton: ToggleSwitchButton, view: View, position: Int) {}
//        }

        @JvmStatic private val ENABLED = true

        @JvmStatic private val LAYOUT_ID = R.layout.toggle_switch_button_view
        @JvmStatic private val LAYOUT_HEIGHT = LayoutParams.WRAP_CONTENT
        @JvmStatic private val LAYOUT_WIDTH = LayoutParams.WRAP_CONTENT

        @JvmStatic private val NUM_ENTRIES = 0

        @JvmStatic private val SEPARATOR_COLOR = R.color.fightPandemicsNero
        @JvmStatic private val SEPARATOR_VISIBLE = false

        @JvmStatic private val TEXT_SIZE = 12f

        @JvmStatic private val TOGGLE_DISTANCE = 0f
        @JvmStatic private val TOGGLE_ELEVATION = 0f
        @JvmStatic private val TOGGLE_HEIGHT = 38f
        @JvmStatic private val TOGGLE_WIDTH = 72f

        @JvmStatic private val UNCHECKED_BACKGROUND_COLOR = R.color.fightPandemicsWhiteSmoke
        @JvmStatic private val UNCHECKED_BORDER_COLOR = R.color.fightPandemicsWhiteSmoke
        @JvmStatic private val UNCHECKED_TEXT_COLOR = R.color.fightPandemicsNero
    }

    /*
     * Instance Variables
     */
    private var checkedBackgroundColor: Int
    private var checkedBorderColor: Int
    private var checkedTextColor: Int
    private val opacity = 0.6F

    private var borderRadius: Float
    private var borderWidth: Float

    private var uncheckedBackgroundColor: Int
    private var uncheckedBorderColor: Int
    var uncheckedTextColor: Int

    private var separatorColor: Int
    private var separatorVisible = SEPARATOR_VISIBLE

    private var textSize: Float

    private var toggleElevation = TOGGLE_ELEVATION
    private var toggleMargin: Float
    private var toggleHeight: Float
    private var toggleWidth: Float

    private var layoutHeight = LAYOUT_HEIGHT
    private var layoutWidth = LAYOUT_WIDTH

    private var layoutId = LAYOUT_ID
    private var numEntries = NUM_ENTRIES

//    var prepareDecorator: ToggleSwitchButton.ToggleSwitchButtonDecorator = EMPTY_TOGGLE_DECORATOR
    private var checkedDecorator: ToggleSwitchButton.ViewDecorator? = null
    private var uncheckedDecorator: ToggleSwitchButton.ViewDecorator? = null

    var buttons = ArrayList<ToggleSwitchButton>()

    /*
     * Constructors
     */
    constructor(context: Context) : super(context) {

        // Setup View
        setUpView()

        // Set default params
        checkedBackgroundColor = ContextCompat.getColor(context, CHECKED_BACKGROUND_COLOR)
        checkedBorderColor = ContextCompat.getColor(context, CHECKED_BORDER_COLOR)
        checkedTextColor = ContextCompat.getColor(context, CHECKED_TEXT_COLOR)

        borderRadius = dp2px(context, BORDER_RADIUS_DP.toFloat())
        borderWidth = dp2px(context, BORDER_WIDTH.toFloat())

        uncheckedBackgroundColor = ContextCompat.getColor(context, UNCHECKED_BACKGROUND_COLOR)
        uncheckedBorderColor = ContextCompat.getColor(context, UNCHECKED_BORDER_COLOR)
        uncheckedTextColor = ContextCompat.getColor(context, UNCHECKED_TEXT_COLOR)

        separatorColor = ContextCompat.getColor(context, SEPARATOR_COLOR)

        textSize = dp2px(context, TEXT_SIZE)

        toggleMargin = dp2px(getContext(), TOGGLE_DISTANCE)
        toggleHeight = dp2px(getContext(), TOGGLE_HEIGHT)
        toggleWidth = dp2px(getContext(), TOGGLE_WIDTH)
    }

    @Throws(RuntimeException::class)
    constructor(context: Context, attrs: AttributeSet?) : super (context, attrs) {

        if (attrs != null) {
            setUpView()

            val attributes = context.obtainStyledAttributes(
                attrs,
                R.styleable.BaseToggleSwitch,
                0,
                0
            )

            try {
                checkedBackgroundColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_checkedBackgroundColor,
                        ContextCompat.getColor(context, CHECKED_BACKGROUND_COLOR))

                checkedBorderColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_checkedBorderColor,
                        ContextCompat.getColor(context, CHECKED_BORDER_COLOR))

                checkedTextColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_checkedTextColor,
                        ContextCompat.getColor(context, CHECKED_TEXT_COLOR))

                borderRadius = attributes.getDimensionPixelSize(
                        R.styleable.BaseToggleSwitch_borderRadius,
                        dp2px(context, BORDER_RADIUS_DP.toFloat()).toInt()).toFloat()

                borderWidth = attributes.getDimensionPixelSize(
                        R.styleable.BaseToggleSwitch_borderWidth,
                        dp2px(context, BORDER_WIDTH.toFloat()).toInt()).toFloat()

                isEnabled = attributes.getBoolean(
                        R.styleable.BaseToggleSwitch_android_enabled,
                        ENABLED)

                uncheckedBackgroundColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_uncheckedBackgroundColor,
                        ContextCompat.getColor(context, UNCHECKED_BACKGROUND_COLOR))

                uncheckedBorderColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_uncheckedBorderColor,
                        ContextCompat.getColor(context, UNCHECKED_BORDER_COLOR))

                uncheckedTextColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_uncheckedTextColor,
                        ContextCompat.getColor(context, UNCHECKED_TEXT_COLOR))

                separatorColor = attributes.getColor(
                        R.styleable.BaseToggleSwitch_separatorColor,
                        ContextCompat.getColor(context, SEPARATOR_COLOR))

                separatorVisible = attributes.getBoolean(
                        R.styleable.BaseToggleSwitch_separatorVisible,
                        SEPARATOR_VISIBLE)

                textSize = attributes.getDimensionPixelSize(
                        R.styleable.BaseToggleSwitch_android_textSize,
                        dp2px(context, TEXT_SIZE).toInt()).toFloat()

                toggleElevation = attributes.getDimensionPixelSize(
                        R.styleable.BaseToggleSwitch_elevation,
                        dp2px(context, TOGGLE_ELEVATION).toInt()).toFloat()

                toggleMargin = attributes.getDimension(
                        R.styleable.BaseToggleSwitch_toggleMargin,
                        dp2px(getContext(), TOGGLE_DISTANCE))

                toggleHeight = attributes.getDimension(
                        R.styleable.BaseToggleSwitch_toggleHeight,
                        dp2px(getContext(), TOGGLE_HEIGHT))

                toggleWidth = attributes.getDimension(
                        R.styleable.BaseToggleSwitch_toggleWidth,
                        dp2px(getContext(), TOGGLE_WIDTH))

                layoutHeight = attributes.getLayoutDimension(
                        R.styleable.BaseToggleSwitch_android_layout_height,
                        LAYOUT_HEIGHT)

                layoutWidth = attributes.getLayoutDimension(
                        R.styleable.BaseToggleSwitch_android_layout_width,
                        LAYOUT_WIDTH)

                val entries = attributes.getTextArray(R.styleable.BaseToggleSwitch_android_entries)
                if (entries == null || entries.isEmpty()) {

                    val entriesList = ArrayList<String>()

                    val textToggleLeft = attributes.getString(R.styleable.BaseToggleSwitch_textToggleLeft)
                    val textToggleRight = attributes.getString(R.styleable.BaseToggleSwitch_textToggleRight)

                    if (!TextUtils.isEmpty(textToggleLeft) && !TextUtils.isEmpty(textToggleRight)) {
                        textToggleLeft?.let{ entriesList.add(textToggleLeft) }
                        val textToggleCenter = attributes.getString(R.styleable.BaseToggleSwitch_textToggleCenter)
                        if (!TextUtils.isEmpty(textToggleCenter)) {
                            textToggleCenter?.let{ entriesList.add(textToggleCenter)}
                        }
                        textToggleRight?.let{ entriesList.add(textToggleRight) }
                        setEntries(entriesList)
                    }
                }
                else {
                    setEntries(entries)
                }
            }
            finally {
                attributes.recycle()
            }
        }
        else {
            throw RuntimeException("AttributeSet is null!")
        }
    }


    /*
     *   Overrode instance methods
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        for (button in buttons) {
            if (layoutWidth != LayoutParams.MATCH_PARENT) {
                button.layoutParams.width = toggleWidth.toInt()
            }

            if (layoutHeight != LayoutParams.MATCH_PARENT) {
                button.layoutParams.height = toggleHeight.toInt()
            }
        }
    }

    final override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        alpha = if (enabled) 1f else opacity
    }

    final override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        if (elevation > 0) {
            clipToPadding = false
            setPadding(elevation.toInt(), elevation.toInt(), elevation.toInt(), elevation.toInt())
        }
        else {
            clipToPadding = true
            setPadding(0, 0, 0, 0)
        }
        for (button in buttons) {
            ViewCompat.setElevation(button, elevation)
        }
    }

    /*
     * Public instance methods
     */
//    fun setEntries(stringArrayId: Int) {
//        setEntries(resources.getStringArray(stringArrayId))
//    }

//    fun setEntries(entries: Array<String>) {
//        setEntries(entries.toList())
//    }

    private fun setEntries(entries : Array<CharSequence>) {
        val entriesList = ArrayList<String>()
        for (entry in entries) {
            entriesList.add(entry.toString())
        }
        setEntries(entriesList)
    }

    private fun setEntries(entries : List<String>) {

        val prepareDecorator = object: ToggleSwitchButton.ToggleSwitchButtonDecorator {
            override fun decorate(toggleSwitchButton: ToggleSwitchButton, view: View, position: Int) {
                val textView = view.findViewById(R.id.text_view) as TextView
                textView.text = entries[position]
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
        }

        val checkedDecorator = object: ToggleSwitchButton.ViewDecorator {
            override fun decorate(view: View, position: Int) {
                val textView = view.findViewById(R.id.text_view) as TextView
                textView.setTextColor(checkedTextColor)
            }
        }

        val uncheckedDecorator = object: ToggleSwitchButton.ViewDecorator {
            override fun decorate(view: View, position: Int) {
                val textView = view.findViewById(R.id.text_view) as TextView
                textView.setTextColor(uncheckedTextColor)
            }
        }

        setView(entries.size, prepareDecorator, checkedDecorator, uncheckedDecorator)
    }

//    fun setView(layoutId: Int, numEntries: Int,
//                prepareDecorator: ToggleSwitchButton.ToggleSwitchButtonDecorator) {
//        setView(layoutId, numEntries, prepareDecorator, null, null)
//    }

    private fun setView(numEntries: Int,
                prepareDecorator: ToggleSwitchButton.ToggleSwitchButtonDecorator,
                checkedDecorator: ToggleSwitchButton.ViewDecorator?,
                uncheckedDecorator: ToggleSwitchButton.ViewDecorator?) {
        removeAllViews()
        buttons.clear()

        this.layoutId = R.layout.toggle_switch_button_view
        this.numEntries = numEntries
        this.checkedDecorator = checkedDecorator
        this.uncheckedDecorator = uncheckedDecorator

        for (index in 0 until numEntries) {
            val positionType = getPosition(index, numEntries)
            val button = ToggleSwitchButton(context, index, positionType, this,
                    layoutId, prepareDecorator, checkedDecorator, uncheckedDecorator,
                    checkedBackgroundColor, checkedBorderColor,
                    borderRadius, borderWidth, uncheckedBackgroundColor,
                    uncheckedBorderColor, separatorColor, toggleMargin.toInt())

            if (index == 0)
                button.check()

            buttons.add(button)
            addView(button)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            elevation = toggleElevation
        }

        manageSeparatorVisibility()
    }

//    fun reDraw() {
//        setView(layoutId, numEntries, prepareDecorator, checkedDecorator, uncheckedDecorator)
//        onRedrawn()
//    }

    /*
       Protected instance methods
     */
    protected abstract fun onRedrawn()

    protected fun manageSeparatorVisibility() {
        for ((index, button) in buttons.withIndex()) {
            if (separatorVisible && index < buttons.size - 1) {
                if (borderWidth <= 0f && toggleMargin <= 0) {
                    button.setSeparatorVisibility(button.isChecked == buttons[index + 1].isChecked)
                }
            }
            else {
                button.setSeparatorVisibility(false)
            }
        }
    }

    private fun getPosition(index: Int, size: Int): ToggleSwitchButton.PositionType {
        return when (index) {
            0 -> {
                ToggleSwitchButton.PositionType.LEFT
            }
            size - 1 -> {
                ToggleSwitchButton.PositionType.RIGHT
            }
            else -> {
                ToggleSwitchButton.PositionType.CENTER
            }
        }
    }

    private fun setUpView() {
        layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT)
        orientation = HORIZONTAL
    }
}
