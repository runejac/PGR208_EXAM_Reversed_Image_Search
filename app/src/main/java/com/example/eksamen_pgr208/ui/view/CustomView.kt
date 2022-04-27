package com.example.eksamen_pgr208.ui.view

import android.R
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import android.widget.TextView


class CustomView : View {
    private val rect : Rect = Rect()
    private val paint : Paint = Paint()

    val view : CustomView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    val text = "Reversed Image Search"
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Typeface.createFromAsset(resources, )
        view?.elevation
        canvas!!.getClipBounds(rect)
        val cHeight: Int = rect.height()
        val cWidth: Int = rect.width()
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 80.toFloat()
        paint.setColor(Color.DKGRAY)
        paint.getTextBounds(text, 0, text.length, rect)
        val x: Float = cWidth / 2f - rect.width() / 2f - rect.left
        val y: Float = cHeight / 2f + rect.height() / 2f - rect.bottom
        canvas.drawText(text, x, y, paint)

    }






}