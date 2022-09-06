package com.bottlerocketstudios.compose.utils.map

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

class ClusterMarkerTextView : TextView {
    private var offsetTop = 0
    private var offsetLeft = 0

    companion object {
        const val NO_OFFSET = 0
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width: Int = measuredWidth
        val height: Int = measuredHeight
        val dimension = width.coerceAtLeast(height)
        if (width > height) {
            offsetTop = width - height
            offsetLeft = NO_OFFSET
        } else {
            offsetTop = NO_OFFSET
            offsetLeft = height - width
        }
        setMeasuredDimension(dimension, dimension)
    }

    override fun draw(canvas: Canvas) {
        canvas.translate((offsetLeft / 2).toFloat(), (offsetTop / 2).toFloat())
        super.draw(canvas)
    }
}
