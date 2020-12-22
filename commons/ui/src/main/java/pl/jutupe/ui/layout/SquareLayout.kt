package pl.jutupe.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import pl.jutupe.ui.R

class SquareLayout: LinearLayout {
    private var inheritOption: InheritFrom = InheritFrom.INHERIT_WIDTH

    constructor(context: Context?): super(context) {
        initialize(null)
    }
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        initialize(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val ta = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout)
        val inheritValue = ta.getInt(R.styleable.SquareLayout_inheritFrom, 0)
        inheritOption = when(inheritValue) {
            0 -> InheritFrom.INHERIT_WIDTH
            1 -> InheritFrom.INHERIT_HEIGHT
            else -> InheritFrom.INHERIT_WIDTH
        }
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when(inheritOption){
            InheritFrom.INHERIT_WIDTH -> super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            InheritFrom.INHERIT_HEIGHT -> super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        }
    }

    private enum class InheritFrom {
        INHERIT_WIDTH,
        INHERIT_HEIGHT
    }
}