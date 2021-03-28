package pl.jutupe.main.util

import androidx.constraintlayout.motion.widget.MotionLayout

class TransitionCompletedListener(
    private val block: () -> Unit
) : MotionLayout.TransitionListener {
    override fun onTransitionStarted(
        p0: MotionLayout?, p1: Int, p2: Int
    ) = Unit

    override fun onTransitionChange(
        p0: MotionLayout?, p1: Int, p2: Int, p3: Float
    ) = Unit

    override fun onTransitionTrigger(
        p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float
    ) = Unit

    override fun onTransitionCompleted(
        p0: MotionLayout?, p1: Int
    ) {
        block.invoke()
    }
}