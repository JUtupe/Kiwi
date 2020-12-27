package pl.jutupe.home.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

class NavigationIconClickListener @JvmOverloads internal constructor(
    context: Context,
    private val content: View,
    private val backdrop: View,
    private val openIcon: Drawable? = null,
    private val closeIcon: Drawable? = null
) : View.OnClickListener {

    private val animationInterpolator = AccelerateDecelerateInterpolator()
    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(view: View) {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.apply {
            removeAllListeners()
            end()
            cancel()
        }

        updateIcon(view)

        val translateY = if (backdropShown) backdrop.height else 0

        // Create and play current animation
        ObjectAnimator.ofFloat(content, "translationY", translateY.toFloat())
            .apply {
                duration = 300
                interpolator = animationInterpolator
                animatorSet.play(this)
                start()
            }
    }

    private fun updateIcon(view: View) {
        if (openIcon == null || closeIcon == null) return

        if (view !is ImageView) {
            throw IllegalArgumentException("updateIcon() must be called on an ImageView")
        }

        val icon =
            if (backdropShown) closeIcon
            else openIcon

        view.setImageDrawable(icon)
    }
}