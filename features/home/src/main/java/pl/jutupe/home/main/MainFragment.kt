package pl.jutupe.home.main

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentMainBinding
import pl.jutupe.home.util.NavigationIconClickListener

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    layoutId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by viewModel()

    override fun onInitDataBinding() {
        binding.filterIcon.setOnClickListener(
            NavigationIconClickListener(
                requireContext(),
                binding.content,
                binding.backdrop,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter),
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up),
            )
        )
    }
}