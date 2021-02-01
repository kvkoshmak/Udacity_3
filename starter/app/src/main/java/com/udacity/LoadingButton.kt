package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

private const val OFFSET_BUTTON = 30
private const val CORNER_RADIUS = 25F
private const val TEXT_SIZE = 55F


enum class DownloadingLabel(val label: Int) {
    DOWNLOAD(R.string.download),
    LOADING(R.string.we_are_loading);

    fun next() = when (this) {
        DOWNLOAD -> LOADING
        LOADING -> DOWNLOAD
    }
}

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var labelButton=DownloadingLabel.DOWNLOAD
    private var valueAnimator = ValueAnimator()
    private var progress = 0F

//    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
//        when (new){
//            ButtonState.Clicked -> print("Clicked")
//            ButtonState.Loading -> animatedButton()
//            ButtonState.Completed -> print("complete")
//        }
//    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Paint styles used for rendering are initialized here. This
        // is a performance optimization, since onDraw() is called
        // for every screen refresh.
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        isClickable = true
    }
    override fun performClick(): Boolean {

        labelButton = labelButton.next()
        animatedButton()
        invalidate()
        if (super.performClick()) return true
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Main button
        paint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRoundRect(0.toFloat(), 0.toFloat(), widthSize.toFloat(), heightSize.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)

        // LOADING animation
        if (labelButton == DownloadingLabel.LOADING){
            //Progress bar of button
            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas.drawRoundRect(0f, 0f, progress/360*widthSize.toFloat(), heightSize.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
            //circle animation
            paint.color = resources.getColor(R.color.colorAccent)
            canvas.drawArc(widthSize.toFloat()/ 2+200f ,heightSize.toFloat() / 2 - 50f, widthSize.toFloat()/2+300f,
                    heightSize.toFloat() / 2 + 50f, 0F, progress, true,paint)
        }

        //Draw text over all the animations
        paint.color = Color.WHITE
        val labelText = resources.getString(labelButton.label)
        canvas.drawText(labelText, widthSize / 2F, heightSize / 1.5F, paint)
    }

    private fun animatedButton() {
        valueAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
            duration = 3000
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                invalidate()
            }
            disableViewDuringAnimation()
            start()
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

    private fun stopAnimations() {
        labelButton = labelButton.next()
        progress = 0F
        invalidate()
    }

    private fun ValueAnimator.disableViewDuringAnimation() {

        // This extension method listens for start/end events on an animation and disables
        // the given view for the entirety of that animation.

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                this@LoadingButton.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                this@LoadingButton.isEnabled = true
                //refresh the button state
                stopAnimations()
            }
        })
    }

}