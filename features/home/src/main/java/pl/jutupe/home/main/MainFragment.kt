package pl.jutupe.home.main

import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    layoutId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by viewModel()

    override fun onInitDataBinding() {

    }
}