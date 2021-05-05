package pl.jutupe.home.util

import androidx.recyclerview.widget.RecyclerView

class ScrollListener(
    private val whenScrollUp: () -> Unit,
    private val whenScrollDown: () -> Unit
) : RecyclerView.OnScrollListener() {
    private var direction: Int = IDLE

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (RecyclerView.SCROLL_STATE_DRAGGING == newState || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            when (direction) {
                UP -> whenScrollUp.invoke()
                DOWN -> whenScrollDown.invoke()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        direction = if (dy >= IDLE) UP else DOWN
    }

    companion object {
        const val UP = 1
        const val DOWN = -1
        const val IDLE = 0
    }
}