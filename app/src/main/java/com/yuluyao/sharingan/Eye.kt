package com.yuluyao.sharingan

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

/**
 * 眼睛
 * 测量，支持padding，计算半径，画背景
 */
open class Eye : View {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  protected val paint: Paint = Paint()

  init {
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3 * Resources.getSystem().displayMetrics.density
    paint.isAntiAlias = true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // 当宽高不相等时，取较小值，保证测量后是一个正方形，宽高相等
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)
    Log.v("Eye", "widthSize:$widthSize,heightSize:$heightSize");
    if (widthSize <= heightSize) {
      setMeasuredDimension(getViewSize(widthMeasureSpec), getViewSize(widthMeasureSpec))
    } else {
      setMeasuredDimension(getViewSize(heightMeasureSpec), getViewSize(heightMeasureSpec))
    }
  }


  private fun getViewSize(measureSpec: Int): Int {
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    val minSize = min(suggestedMinimumWidth, suggestedMinimumHeight)
    return when (specMode) {
      MeasureSpec.UNSPECIFIED -> {
        specSize
      }
      MeasureSpec.AT_MOST -> {
        minSize
      }
      MeasureSpec.EXACTLY -> {
        specSize
      }
      else -> 0
    }
  }

  // 眼睛整个半径，包含外部黑色圆圈
  protected var mRadiusWithBorder = 0F

  // 眼睛半径，不包含外部黑色圆圈
  protected var mRadius = 0F

  // 圆心坐标
  protected var centerCoordinate = floatArrayOf(0f, 0f)

  private val border = Path()

  private val gradient by lazy {
    RadialGradient(0f, 0f, mRadius, intArrayOf(0x44000000, 0x00000000, 0x44000000), floatArrayOf(0f, 0.6f, 1f),
      Shader.TileMode.CLAMP)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    mRadiusWithBorder = min(w.toFloat() - paddingLeft - paddingRight, h.toFloat() - paddingTop - paddingBottom) / 2
    mRadius = mRadiusWithBorder * 0.94f
    centerCoordinate[0] = (w.toFloat() + paddingLeft - paddingRight) / 2
    centerCoordinate[1] = (h.toFloat() + paddingTop - paddingBottom) / 2
    border.addCircle(0f, 0f, mRadiusWithBorder, Path.Direction.CW)
    border.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
    border.fillType = Path.FillType.WINDING
  }

  override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // out border
    paint.color = Color.BLACK
    paint.style = Paint.Style.FILL
    canvas.drawPath(border, paint)

    // fill with red
    paint.color = 0xffac0003.toInt()
    paint.style = Paint.Style.FILL
    canvas.drawCircle(0f, 0f, mRadius, paint)

    // gradient shader
    paint.shader = gradient
    canvas.drawCircle(0f, 0f, mRadius, paint)
    paint.shader = null

    canvas.restore()
  }

}
