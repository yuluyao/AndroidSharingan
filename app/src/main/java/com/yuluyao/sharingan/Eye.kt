package com.yuluyao.sharingan

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

open class Eye : View {
  companion object {
    /**
     * 最小尺寸
     */
    private var MIN_SIZE = 48 * Resources.getSystem().displayMetrics.density
  }

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  protected val paint: Paint = Paint()
  /**
   * 眼睛的半径，外圆减去黑圈
   */
  protected var mRadius = 0f
  /**
   * 外圆的半径
   */
  private var mRadiusOuter = 0f

  init {
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3 * Resources.getSystem().displayMetrics.density
    paint.isAntiAlias = true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    Log.v("Eye", "widthSize:${MeasureSpec.getSize(widthMeasureSpec)},heightSize:${MeasureSpec.getSize(heightMeasureSpec)}")
    setMeasuredDimension(getViewSize(widthMeasureSpec), getViewSize(heightMeasureSpec))
  }


  private fun getViewSize(measureSpec: Int): Int {
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    return when (specMode) {
      MeasureSpec.UNSPECIFIED -> {
        MIN_SIZE.toInt()
      }
      MeasureSpec.AT_MOST -> {
        MIN_SIZE.toInt()
      }
      MeasureSpec.EXACTLY -> {
        specSize
      }
      else -> MIN_SIZE.toInt()
    }
  }


  protected var mWidth: Float = 0f
  protected var mHeight: Float = 0f
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    mWidth = w.toFloat()
    mHeight = h.toFloat()
    mRadiusOuter = min(mWidth, mHeight) / 2
    mRadius = mRadiusOuter * 0.94f
  }

  private val ring = Path()
  override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvas.translate(mWidth / 2, mHeight / 2)

    // out ring
    paint.style = Paint.Style.FILL
    paint.color = Color.BLACK
    ring.addCircle(0f, 0f, mRadiusOuter, Path.Direction.CW)
    ring.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
    ring.fillType = Path.FillType.WINDING
    canvas.drawPath(ring, paint)

    // red background
    paint.style = Paint.Style.FILL
    paint.color = 0xffac0003.toInt()
    canvas.drawCircle(0f, 0f, mRadius, paint)

    // shader
    paint.shader = obtainShader()
    canvas.drawCircle(0f, 0f, mRadius, paint)
    paint.shader = null

    canvas.restore()
  }

  private var shader: Shader? = null
  private fun obtainShader(): Shader {
    if (shader == null) {
      shader = RadialGradient(0f, 0f, mRadius,
        intArrayOf(0x44000000, 0x00000000, 0x44000000), floatArrayOf(0f, 0.6f, 1f),
        Shader.TileMode.CLAMP)
    }
    return shader!!
  }

}