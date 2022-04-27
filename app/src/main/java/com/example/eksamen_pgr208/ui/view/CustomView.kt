package com.example.eksamen_pgr208.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View


class CustomView : View{

    private val rect : Rect = Rect()
    private val paint : Paint = Paint()

    val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val SQUARE_SIZE = 100

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    val text = "Reversed Image Search"
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.getClipBounds(rect)
        val cHeight: Int = rect.height()
        val cWidth: Int = rect.width()
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 80.toFloat()
        paint.getTextBounds(text, 0, text.length, rect)
        val x: Float = cWidth / 2f - rect.width() / 2f - rect.left
        val y: Float = cHeight / 2f + rect.height() / 2f - rect.bottom
        canvas.drawText(text, x, y, paint)








    }


}