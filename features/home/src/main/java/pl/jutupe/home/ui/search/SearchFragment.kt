package pl.jutupe.home.ui.search

import androidx.core.content.ContextCompat
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentSearchBinding
import pl.jutupe.home.util.NavigationIconClickListener

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    layoutId = R.layout.fragment_search
) {
    override val viewModel: SearchViewModel by viewModel()

    override fun onInitDataBinding() {
        binding.filterIcon.setOnClickListener(
            NavigationIconClickListener(
                requireContext(),
                binding.content,
                binding.backdrop.root,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter),
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up),
            )
        )
    }
}