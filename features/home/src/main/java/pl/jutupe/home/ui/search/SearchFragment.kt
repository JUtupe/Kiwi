package pl.jutupe.home.ui.search

import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentSearchBinding
import pl.jutupe.home.util.BackdropManager
import timber.log.Timber

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    layoutId = R.layout.fragment_search
) {
    override val viewModel: SearchViewModel by viewModel()
    private val backdropViewModel: SearchBackdropViewModel by viewModel()

    private lateinit var backdropManager: BackdropManager

    override fun onInitDataBinding() {
        Timber.d("onInitDataBinding")
        binding.viewModel = viewModel
        binding.backdropViewModel = backdropViewModel

        viewModel.events.observe(viewLifecycleOwner, this::onViewEvent)

        backdropManager = BackdropManager(
            binding.searchIcon,
            binding.content,
            binding.backdrop.root,
            openIconRes = R.drawable.ic_search,
            closeIconRes = R.drawable.ic_arrow_up,
        )

        binding.searchIcon.setOnClickListener {
            backdropManager.toggle()
        }

        backdropViewModel.searchText.observe(viewLifecycleOwner) { value ->
            viewModel.onSearchTextChanged(value)
        }
    }

    private fun onViewEvent(event: SearchViewEvent) {
        when (event) {
            is SearchViewEvent.SetBackdropSearchTitle -> {
                binding.title.text = getString(R.string.label_search_result_for, event.text)
            }
            SearchViewEvent.SetBackdropRecentlySearchedTitle ->
                binding.title.text = getString(R.string.label_recently_searched)
        }
    }

    override fun onResume() {
        super.onResume()

        backdropManager.open()
    }
}