package pl.jutupe.home.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import timber.log.Timber

class BackdropManager(
    private val button: ImageView,
    private val content: View,
    private val backdrop: View,
    @DrawableRes private val openIconRes: Int? = null,
    @DrawableRes private val closeIconRes: Int? = null,
) {

    private val animationDuration: Long = 300
    private val animationInterpolator = AccelerateDecelerateInterpolator()
    private val animatorSet = AnimatorSet()

    private var backdropShown: Boolean = false

    var onStateChangedListener: ((isVisible: Boolean) -> Unit)? = null

    fun toggle() {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.apply {
            removeAllListeners()
            end()
            cancel()
        }

        updateIcon()

        val translateY = if (backdropShown) backdrop.height else 0

        // Create and play current animation
        ObjectAnimator.ofFloat(content, "translationY", translateY.toFloat())
            .apply {
                duration = animationDuration
                interpolator = animationInterpolator
                animatorSet.play(this)
                start()
            }

        onStateChangedListener?.invoke(backdropShown)
    }

    fun open() {
        if (!backdropShown) {
            toggle()
        }
    }

    fun close() {
        if (backdropShown) {
            toggle()
        }
    }

    fun isVisible() = backdropShown

    private fun updateIcon() {
        if (openIconRes == null || closeIconRes == null) return

        val icon =
            if (backdropShown) closeIconRes
            else openIconRes

        button.setImageResource(icon)
    }
}