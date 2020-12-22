package pl.jutupe.home

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseActivity
import pl.jutupe.home.databinding.ActivityMainBinding
import pl.jutupe.home.songs.SongAdapter

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(
    layoutId = R.layout.activity_main
) {
    private val songAdapter = SongAdapter()

    override val viewModel: MainActivityViewModel by viewModel()

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