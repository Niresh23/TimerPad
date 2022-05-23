package com.niksama3.timerpad.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class CircleSliderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {
    private var radius = 0f
    private var startPointAngle: Float = -90f
    private var widthOfStroke = 60f
    private var sweepAngle = 180f
    private var endPoint = EdgePoint(widthOfStroke)
    private var startPoint = EdgePoint(widthOfStroke)
    private var centerPaint = Paint()
    private var backgroundStrokeColor = Color.parseColor("#E9E9FF")
    private var strokeColor = Color.parseColor("#7012CE")

    init {
        centerPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }


    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height) / 2.0).toFloat()
        endPoint.computeXYForPoint(sweepAngle, widthOfStroke, width / 2f, height / 2f)
        startPoint.computeXYForPoint(startPointAngle, widthOfStroke, width / 2f, height / 2f)
    }


    private fun radiansToDegrees(radians: Float): Float {
        return (180 / PI).toFloat() * radians
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawStrokeLayer(canvas)
        drawArcLayer(canvas)
        clearCenter(canvas)
        paint.color = Color.BLACK
        startPoint.onDraw(canvas)
        endPoint.onDraw(canvas)
    }

    private fun drawStrokeLayer(canvas: Canvas) {
        paint.color = backgroundStrokeColor
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius,
            paint
        )
    }

    private fun drawArcLayer(canvas: Canvas) {
        paint.color = strokeColor
        canvas.drawArc(
            (width / 2f) - radius,
            height / 2f - radius,
            (width / 2f) + radius,
            height / 2f + radius,
            startPointAngle,
            sweepAngle + startPointAngle.absoluteValue,
            true,
            paint
        )
    }

    private fun clearCenter(canvas: Canvas) {
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius - 2 * widthOfStroke,
            centerPaint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val angle = getAngle(event.x, event.y, startPointAngle)
                endPoint.computeXYForPoint(angle, widthOfStroke, width / 2f, height / 2f)

                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

    private fun getAngle(x: Float, y: Float, startAngle: Float): Float {
        var add = 0f
        val a = x - (width / 2)
        val b = (height / 2) - y
        val c = hypot(a, b)
        val angle: Float = if((b < 0 && a < 0) || (b < 0 && a > 0))
            360f - radiansToDegrees(acos(a / c))
        else radiansToDegrees(acos(a / c))
        Log.d("CircleSlider", "a = $a , b = $b -> $angle degree")
        return 360 - angle
    }
}

private fun degreesToRadians(degree: Float): Float {
    return (PI / 180).toFloat() * degree
}


class EdgePoint(private val pointRadius: Float) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var pointPosition: PointF = PointF(0f, 0f)

    fun onDraw(canvas: Canvas) {
        canvas.drawCircle(pointPosition.x, pointPosition.y, pointRadius, paint)
    }

    fun contains(): Boolean {
        return false
    }

    fun computeXYForPoint (angle: Float, radius: Float, ordinateX: Float, ordinateY: Float) {
        val x = (radius - pointRadius) * cos(degreesToRadians(angle)) + ordinateX
        val y = (radius - pointRadius) * sin(degreesToRadians(angle)) + ordinateY
        pointPosition = PointF(x, y)
    }
}