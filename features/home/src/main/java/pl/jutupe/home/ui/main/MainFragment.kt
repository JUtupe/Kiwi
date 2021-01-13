package pl.jutupe.home.ui.main

import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentMainBinding
import pl.jutupe.ui.util.BackdropManager

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    layoutId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by viewModel()

    private lateinit var backdropManager: BackdropManager

    override fun onInitDataBinding() {
        backdropManager = BackdropManager(
            binding.filterIcon,
            binding.content,
            binding.backdrop.root,
            openIconRes = R.drawable.ic_search,
            closeIconRes = R.drawable.ic_arrow_up,
        )

        binding.filterIcon.setOnClickListener {
            backdropManager.toggle()
        }
    }
}