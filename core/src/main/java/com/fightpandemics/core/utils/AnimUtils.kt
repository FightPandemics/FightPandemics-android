package com.fightpandemics.core.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator

object AnimUtils {
    const val DEFAULT_DURATION = -1
    const val NO_DELAY = 0
    val EASE_IN: Interpolator = PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f)
    val EASE_OUT: Interpolator = PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f)
    val EASE_OUT_EASE_IN: Interpolator = PathInterpolator(0.4f, 0F, 0.2f, 1F)
    fun crossFadeViews(fadeIn: View, fadeOut: View, duration: Int) {
        fadeIn(fadeIn, duration)
        fadeOut(fadeOut, duration)
    }

    fun fadeOut(fadeOut: View, duration: Int) {
        fadeOut(fadeOut, duration, null)
    }

    fun fadeOut(fadeOut: View, durationMs: Int, callback: AnimationCallback?) {
        fadeOut.alpha = 1f
        val animator = fadeOut.animate()
        animator.cancel()
        animator
            .alpha(0f)
            .withLayer()
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        fadeOut.visibility = View.GONE
                        callback?.onAnimationEnd()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        fadeOut.visibility = View.GONE
                        fadeOut.alpha = 0f
                        callback?.onAnimationCancel()
                    }
                })
        if (durationMs != DEFAULT_DURATION) {
            animator.duration = durationMs.toLong()
        }
        animator.start()
    }

    @JvmOverloads
    fun fadeIn(
        fadeIn: View,
        durationMs: Int,
        delay: Int = NO_DELAY,
        callback: AnimationCallback? = null
    ) {
        fadeIn.alpha = 0f
        val animator = fadeIn.animate()
        animator.cancel()
        animator.startDelay = delay.toLong()
        animator
            .alpha(1f)
            .withLayer()
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        fadeIn.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        fadeIn.alpha = 1f
                        callback?.onAnimationCancel()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        callback?.onAnimationEnd()
                    }
                })
        if (durationMs != DEFAULT_DURATION) {
            animator.duration = durationMs.toLong()
        }
        animator.start()
    }

    /**
     * Scales in the view from scale of 0 to actual dimensions.
     *
     * @param view The view to scale.
     * @param durationMs The duration of the scaling in milliseconds.
     * @param startDelayMs The delay to applying the scaling in milliseconds.
     */
    fun scaleIn(view: View, durationMs: Int, startDelayMs: Int) {
        val listener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
                view.scaleX = 1f
                view.scaleY = 1f
            }
        }
        scaleInternal(
            view,
            0 /* startScaleValue */,
            1 /* endScaleValue */,
            durationMs,
            startDelayMs,
            listener,
            EASE_IN
        )
    }

    /**
     * Scales out the view from actual dimensions to 0.
     *
     * @param view The view to scale.
     * @param durationMs The duration of the scaling in milliseconds.
     */
    fun scaleOut(view: View, durationMs: Int) {
        val listener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
                view.visibility = View.GONE
                view.scaleX = 0f
                view.scaleY = 0f
            }
        }
        scaleInternal(
            view,
            1 /* startScaleValue */,
            0 /* endScaleValue */,
            durationMs,
            NO_DELAY,
            listener,
            EASE_OUT
        )
    }

    private fun scaleInternal(
        view: View,
        startScaleValue: Int,
        endScaleValue: Int,
        durationMs: Int,
        startDelay: Int,
        listener: AnimatorListenerAdapter,
        interpolator: Interpolator
    ) {
        view.scaleX = startScaleValue.toFloat()
        view.scaleY = startScaleValue.toFloat()
        val animator = view.animate()
        animator.cancel()
        animator
            .setInterpolator(interpolator)
            .scaleX(endScaleValue.toFloat())
            .scaleY(endScaleValue.toFloat())
            .setListener(listener)
            .withLayer()
        if (durationMs != DEFAULT_DURATION) {
            animator.duration = durationMs.toLong()
        }
        animator.startDelay = startDelay.toLong()
        animator.start()
    }

    /**
     * Animates a view to the new specified dimensions.
     *
     * @param view The view to change the dimensions of.
     * @param newWidth The new width of the view.
     * @param newHeight The new height of the view.
     */
    fun changeDimensions(view: View, newWidth: Int, newHeight: Int) {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        val oldWidth = view.width
        val oldHeight = view.height
        val deltaWidth = newWidth - oldWidth
        val deltaHeight = newHeight - oldHeight
        animator.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            view.layoutParams.width = (value * deltaWidth + oldWidth).toInt()
            view.layoutParams.height = (value * deltaHeight + oldHeight).toInt()
            view.requestLayout()
        }
        animator.start()
    }

    class AnimationCallback {
        fun onAnimationEnd() {}
        fun onAnimationCancel() {}
    }
}
