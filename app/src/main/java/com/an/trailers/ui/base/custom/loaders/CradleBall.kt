package com.an.trailers.ui.base.custom.loaders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.an.trailers.R


class CradleBall : View {

    private var ballWidth: Int = 0
    private var ballHeight: Int = 0

    private var paint: Paint? = null

    private var loadingColor = Color.WHITE

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CradleBall)
            loadingColor = typedArray.getColor(R.styleable.CradleBall_cradle_ball_color, Color.WHITE)
            typedArray.recycle()
        }
        paint = Paint()
        paint!!.color = loadingColor
        paint!!.style = Paint.Style.FILL
        paint!!.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ballWidth = w
        ballHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle((ballWidth / 2).toFloat(), (ballHeight / 2).toFloat(), (ballWidth / 2).toFloat(), paint!!)
    }

    fun setLoadingColor(color: Int) {
        loadingColor = color
        paint!!.color = color
        postInvalidate()
    }

    fun getLoadingColor(): Int {
        return loadingColor
    }
}