package pl.jutupe.home.ui.search

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.adapter.search.SearchItemAdapter
import pl.jutupe.home.databinding.FragmentSearchBinding
import pl.jutupe.home.ui.search.SearchViewModel.SearchViewEvent
import pl.jutupe.home.util.ScrollListener
import pl.jutupe.ui.util.BackdropManager
import timber.log.Timber

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    layoutId = R.layout.fragment_search
) {
    private val searchAdapter = SearchItemAdapter()

    override val viewModel: SearchViewModel by viewModel()
    private val backdropViewModel: SearchBackdropViewModel by viewModel()

    private lateinit var backdropManager: BackdropManager

    private val onScrollListener by lazy {
        ScrollListener(
            whenScrollUp = backdropManager::close,
            whenScrollDown = backdropManager::open
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }

    override fun onInitDataBinding() {
        Timber.d("onInitDataBinding")
        binding.viewModel = viewModel
        binding.backdropViewModel = backdropViewModel
        searchAdapter.action = viewModel.songAction

        viewModel.events.observe(viewLifecycleOwner, this::onViewEvent)

        binding.list.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }

        backdropManager = BackdropManager(
            binding.searchIcon,
            binding.content,
            binding.backdrop.root,
            openIconRes = R.drawable.ic_search,
            closeIconRes = R.drawable.ic_arrow_up,
        )

        binding.list.addOnScrollListener(onScrollListener)

        binding.searchIcon.setOnClickListener {
            backdropManager.toggle()
        }

        backdropViewModel.searchText.observe(viewLifecycleOwner) { value ->
            viewModel.onSearchTextChanged(value)
        }
    }

    private fun onViewEvent(event: SearchViewEvent) {
        when (event) {
            is SearchViewEvent.SetBackdropSearchTitle ->
                binding.title.text = getString(R.string.label_search_result_for, event.text)
            SearchViewEvent.SetBackdropRecentlySearchedTitle ->
                binding.title.text = getString(R.string.label_recently_searched)
            SearchViewEvent.RefreshAdapter ->
                searchAdapter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()

        backdropManager.open()
    }
}