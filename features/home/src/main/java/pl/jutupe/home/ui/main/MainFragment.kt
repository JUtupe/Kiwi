package pl.jutupe.home.ui.main

import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.adapter.WrapperAdapter
import pl.jutupe.home.adapter.library.MediaItemAdapter
import pl.jutupe.home.adapter.wrap
import pl.jutupe.home.databinding.FragmentMainBinding
import pl.jutupe.ui.util.BackdropManager

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    layoutId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by viewModel()

    private lateinit var backdropManager: BackdropManager

    private val artistsAdapter = MediaItemAdapter()

    private val adapters by lazy {
        ConcatAdapter(
            artistsAdapter.wrap(
                GridLayoutManager(requireContext(), 3),
                WrapperAdapter.WrapperHeader(getString(R.string.label_random_artists))
            ),
        )
    }

    override fun onInitDataBinding() {
        backdropManager = BackdropManager(
            binding.filterIcon,
            binding.content,
            binding.backdrop.root,
            openIconRes = R.drawable.ic_filter,
            closeIconRes = R.drawable.ic_arrow_up,
        )

        binding.filterIcon.setOnClickListener {
            backdropManager.toggle()
        }

        binding.list.apply {
            adapter = adapters
        }

        lifecycleScope.launch {
            viewModel.artists.collectLatest {
                artistsAdapter.submitData(PagingData.from(it))
            }
        }
    }
}