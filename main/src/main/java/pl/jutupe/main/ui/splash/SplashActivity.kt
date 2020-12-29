package pl.jutupe.main.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import pl.jutupe.main.R
import pl.jutupe.main.ui.main.MainActivity

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<MotionLayout>(R.id.motionLayout)
            .setTransitionListener(
                object : MotionLayout.TransitionListener {
                    override fun onTransitionCompleted(layout: MotionLayout?, p1: Int) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }

                    override fun onTransitionChange(
                        layout: MotionLayout?, p1: Int, p2: Int, p3: Float
                    ) = Unit

                    override fun onTransitionStarted(
                        layout: MotionLayout?, p1: Int, p2: Int
                    ) = Unit

                    override fun onTransitionTrigger(
                        layout: MotionLayout?, p1: Int, p2: Boolean, p3: Float
                    ) = Unit
                }
            )
    }

    override fun onResume() {
        super.onResume()
        findViewById<MotionLayout>(R.id.motionLayout).startLayoutAnimation()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}