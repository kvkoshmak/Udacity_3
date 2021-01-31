package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColor
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import java.util.logging.Level.OFF
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

private const val OFFSET_BUTTON = 30
private const val CORNER_RADIUS = 25F


enum class DownloadingLabel(val label: Int) {
    DOWNLOAD(R.string.download),
    LOADING(R.string.we_are_loading);

    fun next() = when (this) {
        DOWNLOAD -> LOADING
        LOADING -> DOWNLOAD
    }
}
//private enum class ColourButtons {
//    DOWNLOAD,
//    LOADING,
//    OK}

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var labelButton=DownloadingLabel.DOWNLOAD
    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Paint styles used for rendering are initialized here. This
        // is a performance optimization, since onDraw() is called
        // for every screen refresh.
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    init {
        isClickable = true

    }
    override fun performClick(): Boolean {

        labelButton = labelButton.next()
        invalidate()
        if (super.performClick()) return true
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = setColour(labelButton)
        canvas.drawRoundRect(widthSize.toFloat(),heightSize.toFloat(),0.toFloat(),0.toFloat(), CORNER_RADIUS, CORNER_RADIUS,paint)
        paint.color = Color.WHITE
        val labelText = resources.getString(labelButton.label)
        canvas.drawText(labelText,widthSize/2F,heightSize/1.5F,paint)
        if (labelButton == DownloadingLabel.LOADING){
            paint.color = resources.getColor(R.color.colorAccent)
            canvas.drawCircle((widthSize+55/2)/2F,heightSize/1.5F,32f,paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
    private fun setColour(selectColor: DownloadingLabel): Int {
        return when (selectColor){
            DownloadingLabel.DOWNLOAD -> resources.getColor(R.color.colorPrimary)
            DownloadingLabel.LOADING -> resources.getColor(R.color.colorPrimaryDark)
        }
    }

}