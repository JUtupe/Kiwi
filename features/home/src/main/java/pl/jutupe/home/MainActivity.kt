package pl.jutupe.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseActivity
import pl.jutupe.home.databinding.ActivityMainBinding
import pl.jutupe.home.library.LibraryFragment
import pl.jutupe.home.main.MainFragment
import pl.jutupe.home.search.SearchFragment

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(
    layoutId = R.layout.activity_main
) {
    override val viewModel: MainActivityViewModel by viewModel()

    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun onInitDataBinding() {
        binding.viewModel = viewModel

        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        binding.pager.apply {
            adapter = pagerAdapter
        }

        binding.tabs.setupWithViewPager(binding.pager)

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setNavigationOnClickListener {
                //todo onNavigationClicked
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