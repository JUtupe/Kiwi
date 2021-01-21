package pl.jutupe.home.ui

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentHomeBinding
import pl.jutupe.home.ui.library.LibraryFragment
import pl.jutupe.home.ui.main.MainFragment
import pl.jutupe.home.ui.search.SearchFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    layoutId = R.layout.fragment_home
) {
    override val viewModel: HomeFragmentViewModel by viewModel()

    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    val fragments = listOf<Pair<() -> Fragment, Int>>(
        { MainFragment() } to R.string.tab_main,
        { LibraryFragment() } to R.string.tab_library,
        { SearchFragment() } to R.string.tab_search
    )

    override fun onInitDataBinding() {
        binding.viewModel = viewModel

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
    }

    private inner class ScreenSlidePagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int) =
            fragments[position].first.invoke()
    }
}