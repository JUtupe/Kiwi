package pl.jutupe.home

import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseActivity
import pl.jutupe.home.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(
    layoutId = R.layout.activity_main
) {

    override val viewModel: MainActivityViewModel by viewModel()

    override fun onInitDataBinding() {
        binding.viewModel = viewModel
    }
}