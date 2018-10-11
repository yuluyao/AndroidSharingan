package com.yuluyao.sharingan

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

open class Eye : View {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  protected val paint: Paint = Paint()


  protected val density = context.resources.displayMetrics.density
  /**
   * 最小尺寸
   */
  protected var minSize = 48 * density
  /**
   * 眼睛的半径
   */
  protected var mRadius = 0f

  init {
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3 * density
    paint.isAntiAlias = true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    Log.v("Eye", "widthSize:${MeasureSpec.getSize(widthMeasureSpec)}," +
        "heightSize:${MeasureSpec.getSize(heightMeasureSpec)}")
    setMeasuredDimension(getViewSize(widthMeasureSpec), getViewSize(heightMeasureSpec))
  }


  private fun getViewSize(measureSpec: Int): Int {
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    return when (specMode) {
      MeasureSpec.UNSPECIFIED -> {
        minSize.toInt()
      }
      MeasureSpec.AT_MOST -> {
        minSize.toInt()
      }
      MeasureSpec.EXACTLY -> {
        specSize
      }
      else -> minSize.toInt()
    }
  }


  protected var mWidth: Float = 0f
  protected var mHeight: Float = 0f
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    mWidth = w.toFloat()
    mHeight = h.toFloat()
    mRadius = Math.min(mWidth, mHeight) / 2
  }

}