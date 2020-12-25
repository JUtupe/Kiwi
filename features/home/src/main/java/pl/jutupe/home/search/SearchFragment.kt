package pl.jutupe.home.search

import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    layoutId = R.layout.fragment_search
) {
    override val viewModel: SearchViewModel by viewModel()

    override fun onInitDataBinding() {

    }
}