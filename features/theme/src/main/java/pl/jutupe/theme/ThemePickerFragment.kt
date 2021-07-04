package pl.jutupe.theme

import androidx.drawerlayout.widget.DrawerLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseFragment
import pl.jutupe.theme.databinding.FragmentThemePickerBinding

class ThemePickerFragment : BaseFragment<FragmentThemePickerBinding, ThemePickerViewModel>(
    layoutId = R.layout.fragment_theme_picker
) {

    override val viewModel: ThemePickerViewModel by viewModel()

    override fun onInitDataBinding() {
        //


        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setNavigationOnClickListener {
                requireActivity()
                    .findViewById<DrawerLayout>(R.id.drawer)
                    .open()
            }
        }
    }
}
