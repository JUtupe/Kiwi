package pl.jutupe.home.ui

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentHomeBinding
import pl.jutupe.home.ui.library.LibraryFragment
import pl.jutupe.home.ui.main.MainFragment
import pl.jutupe.home.ui.search.SearchFragment
import kotlin.math.abs

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    layoutId = R.layout.fragment_home
) {
    override val viewModel: HomeFragmentViewModel by viewModel()
    val controllerViewModel: ControllerViewModel by viewModel()

    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    private val controllerGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (e1.x - e2.x > SWIPE_MIN_HORIZONTAL_DISTANCE) {
                    controllerViewModel.onLeftSwiped()

                    return true // Right to left
                } else if (e2.x - e1.x > SWIPE_MIN_HORIZONTAL_DISTANCE) {
                    controllerViewModel.onRightSwiped()

                    return true // Left to right
                }
            } else if (abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                if (e1.y - e2.y > SWIPE_MIN_VERTICAL_DISTANCE) {
                    return false // Bottom to top
                } else if (e2.y - e1.y > SWIPE_MIN_VERTICAL_DISTANCE) {
                    controllerViewModel.onDownSwiped()

                    return true // Top to bottom
                }
            }

            return false
        }
    }
    private val gestureDetector = GestureDetector(context, controllerGestureListener)

    val fragments = listOf(
        { MainFragment() } to R.string.tab_main,
        { LibraryFragment() } to R.string.tab_library,
        { SearchFragment() } to R.string.tab_search
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onInitDataBinding() {
        binding.viewModel = viewModel
        binding.controllerViewModel = controllerViewModel

        controllerViewModel.events.observe(viewLifecycleOwner, this::onControllerViewEvent)

        pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager, lifecycle)
        binding.pager.apply {
            adapter = pagerAdapter
            setPageTransformer(MarginPageTransformer(resources.getDimensionPixelSize(R.dimen.margin_fragment)))
            offscreenPageLimit = 2
        }

        TabLayoutMediator(binding.tabs, binding.pager) { tab, pos ->
            tab.text = getString(fragments[pos].second)
        }.attach()

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setNavigationOnClickListener {
                requireActivity()
                    .findViewById<DrawerLayout>(R.id.drawer)
                    .open()
            }
        }

        binding.controller.root.setOnTouchListener { _, event ->
            !gestureDetector.onTouchEvent(event)
        }
    }

    private fun onControllerViewEvent(event: ControllerViewModel.ViewEvent) {
        when (event) {
            is ControllerViewModel.ViewEvent.ChangePlayingButton -> {
                val newIcon = if (event.isPlaying) R.drawable.ic_pause else R.drawable.ic_play

                binding.controller.playPause.setImageResource(newIcon)
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int) =
            fragments[position].first.invoke()
    }

    companion object {
        private const val SWIPE_MIN_HORIZONTAL_DISTANCE = 120
        private const val SWIPE_MIN_VERTICAL_DISTANCE = 40
        private const val SWIPE_THRESHOLD_VELOCITY = 200
    }
}