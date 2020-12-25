package pl.jutupe.base.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

abstract class BaseActivity<B : ViewDataBinding, V : ViewModel>(
    @LayoutRes
    private val layoutId: Int
) : FragmentActivity() {

    lateinit var binding: B
    abstract val viewModel: V

    abstract fun onInitDataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

        onInitDataBinding()
    }
}