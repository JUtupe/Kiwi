package pl.jutupe.home.library

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.home.R
import pl.jutupe.home.databinding.FragmentLibraryBinding
import pl.jutupe.home.songs.SongAdapter

class LibraryFragment : BaseFragment<FragmentLibraryBinding, LibraryViewModel>(
    layoutId = R.layout.fragment_library
) {
    override val viewModel: LibraryViewModel by viewModel()

    private val songAdapter = SongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.songs.collectLatest {
                songAdapter.submitData(it)
            }
        }
    }

    override fun onInitDataBinding() {
        binding.viewModel = viewModel
        songAdapter.action = viewModel.songAction
        binding.list.apply {
            adapter = songAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }
}