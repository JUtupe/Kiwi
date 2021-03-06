package pl.jutupe.home.ui.main

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.adapter.WrapperAdapter
import pl.jutupe.home.adapter.library.MediaItemAdapter
import pl.jutupe.home.adapter.wrap
import pl.jutupe.home.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    layoutId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by viewModel()

    private val artistsAdapter = MediaItemAdapter()
    private val recentlyAddedAdapter = MediaItemAdapter(isCompactMode = true)

    private val adapters by lazy {
        ConcatAdapter(
            artistsAdapter.wrap(
                GridLayoutManager(requireContext(), 3),
                WrapperAdapter.WrapperHeader(getString(R.string.label_random_artists)),
            ),
            recentlyAddedAdapter.wrap(
                GridLayoutManager(requireContext(), 2),
                WrapperAdapter.WrapperHeader(getString(R.string.label_recently_added)),
            ),
        )
    }

    override fun onInitDataBinding() {
        recentlyAddedAdapter.action = viewModel.recentlyAddedAction

        binding.list.apply {
            adapter = adapters
        }

        binding.header.apply {
            backButton.visibility = View.GONE
            extraButton.visibility = View.GONE
            title.text = resources.getStringArray(R.array.welcome_message).random()
        }

        lifecycleScope.launch {
            viewModel.artists.collectLatest {
                artistsAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            viewModel.recentlyAdded.collectLatest {
                recentlyAddedAdapter.submitData(it)
            }
        }
    }
}