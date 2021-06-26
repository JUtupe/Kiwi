package pl.jutupe.home.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
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

        backdropManager = BackdropManager(
            binding.header.extraButton,
            binding.content,
            binding.backdrop.root,
            openIconRes = R.drawable.ic_search,
            closeIconRes = R.drawable.ic_arrow_up,
        )

        binding.list.apply {
            adapter = searchAdapter
            addOnScrollListener(onScrollListener)
        }

        binding.backdrop.input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    viewModel.onSearchDoneClicked()
                    backdropManager.close()

                    true
                }
                else -> false
            }
        }

        binding.header.apply {
            extraButton.setOnClickListener {
                backdropManager.toggle()

                val isContentVisible = backdropManager.isVisible()

                if (isContentVisible) {
                    viewModel.onSearchButtonClicked()
                } else {
                    viewModel.onHideSearchButtonClicked()
                }
            }
            extraButton.visibility = View.VISIBLE
            backButton.visibility = View.GONE
        }

        backdropViewModel.searchText.observe(viewLifecycleOwner) { value ->
            viewModel.onSearchTextChanged(value)
        }
    }

    private fun onViewEvent(event: SearchViewEvent) {
        when (event) {
            is SearchViewEvent.SetBackdropSearchTitle ->
                binding.header.title.text = getString(R.string.label_search_result_for, event.text)
            SearchViewEvent.SetBackdropRecentlySearchedTitle ->
                binding.header.title.text = getString(R.string.label_recently_searched)
            SearchViewEvent.RefreshAdapter ->
                searchAdapter.refresh()
            SearchViewEvent.HideSearchKeyboard -> {
                val inputMethodManager: InputMethodManager? =
                    getSystemService(requireContext(), InputMethodManager::class.java)

                binding.backdrop.input.clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(
                    view?.windowToken,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
            SearchViewEvent.ShowSearchKeyboard -> {
                val inputMethodManager: InputMethodManager? =
                    getSystemService(requireContext(), InputMethodManager::class.java)

                binding.backdrop.input.requestFocus()
                inputMethodManager?.showSoftInput(
                    binding.backdrop.input,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }
    }
}
