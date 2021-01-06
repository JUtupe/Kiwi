package pl.jutupe.home.ui.library

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentLibraryBinding
import pl.jutupe.home.songs.MediaItemAdapter

class LibraryFragment : BaseFragment<FragmentLibraryBinding, LibraryViewModel>(
    layoutId = R.layout.fragment_library
) {
    override val viewModel: LibraryViewModel by viewModel()

    private val mediaItemAdapter = MediaItemAdapter()

    private val libraryBackCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            viewModel.onNavigateToParentClicked()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.songs.collectLatest {
                mediaItemAdapter.submitData(it)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, libraryBackCallback)
    }

    override fun onPause() {
        super.onPause()

        libraryBackCallback.isEnabled = false
    }

    override fun onResume() {
        super.onResume()

        libraryBackCallback.isEnabled = viewModel.isInRoot.value == false
    }

    private fun onViewEvent(event: LibraryViewEvent) {
        when (event) {
            LibraryViewEvent.RefreshAdapter -> mediaItemAdapter.refresh()
        }
    }

    override fun onInitDataBinding() {
        binding.viewModel = viewModel
        mediaItemAdapter.action = viewModel.songAction

        binding.list.apply {
            adapter = mediaItemAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.isInRoot.observe(viewLifecycleOwner) { isRoot ->
            libraryBackCallback.isEnabled = !isRoot
        }

        viewModel.events.observe(viewLifecycleOwner, this::onViewEvent)
    }
}