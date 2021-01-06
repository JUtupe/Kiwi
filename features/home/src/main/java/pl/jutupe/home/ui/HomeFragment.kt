package pl.jutupe.home.ui

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
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

    override fun onInitDataBinding() {
        binding.viewModel = viewModel

        pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager)
        binding.pager.apply {
            adapter = pagerAdapter
            pageMargin = context.resources.getDimensionPixelSize(R.dimen.margin_fragment)
        }

        binding.tabs.setupWithViewPager(binding.pager)

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
        fm: FragmentManager
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        val fragments = listOf<Pair<Fragment, Int>>(
            MainFragment() to R.string.tab_main,
            LibraryFragment() to R.string.tab_library,
            SearchFragment() to R.string.tab_search
        )

        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment = fragments[position].first

        override fun getPageTitle(position: Int): CharSequence =
            getString(fragments[position].second)
    }
}