package com.example.eksamen_pgr208.ui.view

import android.R.attr.textColor
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.TextView


class CustomView : View{

    private val rect : Rect = Rect()
    private val paint : Paint = Paint()
    val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val SQUARE_SIZE = 100

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    val text = "Hello"
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        textPaint.color = textColor
        canvas?.drawColor(Color.CYAN)
        canvas?.drawRect(rect, paint)
        rect.left = 10
        rect.top = 10
        rect.right = rect.left + SQUARE_SIZE
        rect.bottom =  rect.top + SQUARE_SIZE
        canvas?.drawText(text, 2F, 2F, textPaint)
        paint.setColor(Color.CYAN)




    }


}