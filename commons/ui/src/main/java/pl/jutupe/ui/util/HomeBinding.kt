package pl.jutupe.ui.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.bindVisibleOrGone(visibleOrGone: Boolean) {
    visibility =
        if (visibleOrGone) View.VISIBLE
        else View.GONE
}