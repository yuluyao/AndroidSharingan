package com.yuluyao.sharingan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class Sharingan : View {


  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

  }

  private val paint: Paint = Paint()


  private val density = context.resources.displayMetrics.density
  /**
   * 最小尺寸
   */
  var minSize = 48 * density
  /**
   * 眼睛的半径
   */
  private var mRadius = 0f

  init {
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3 * density
    paint.isAntiAlias = true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthMode = MeasureSpec.getMode(widthMeasureSpec)
    val heightMode = MeasureSpec.getMode(heightMeasureSpec)
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)

    Log.v("Sharingan", "widthSize:$widthSize , heightSize:$heightSize")
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


  private var mWidth: Float = 0f
  private var mHeight: Float = 0f
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    mWidth = w.toFloat()
    mHeight = h.toFloat()
    mRadius = Math.min(mWidth, mHeight) / 2

  }


  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    canvas ?: return

    canvas.translate(mWidth / 2, mHeight / 2)

    // red background
    paint.style = Paint.Style.FILL
    paint.color = 0xffac0003.toInt()
    canvas.drawCircle(0f, 0f, mRadius, paint)

    // center dot
    paint.color = 0xff000000.toInt()
    val centerRadius = mRadius * 0.15f
    canvas.drawCircle(0f, 0f, centerRadius, paint)

    // middle ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.02f
    paint.color = 0x66000000.toInt()
    val middleRadius = mRadius * 0.6f
    canvas.drawCircle(0f, 0f, middleRadius, paint)

    // out ring
    paint.strokeWidth = mRadius * 0.06f
    paint.color = 0xff000000.toInt()
    val outRadius = mRadius * 0.97f
    canvas.drawCircle(0f, 0f, outRadius, paint)

    // gou
    paint.style = Paint.Style.FILL
    val gouRadius = mRadius * 0.125f
    for (degree in -45 until 360 step 120) {
      canvas.save()
      canvas.rotate(degree.toFloat())

      // 画勾玉
      val gou = Path()
      gou.addCircle(middleRadius, 0f, gouRadius, Path.Direction.CW)
      val rectF = RectF(middleRadius - gouRadius * 2, 0 - gouRadius,
        middleRadius + gouRadius * 2, 0 + gouRadius * 3)
      gou.arcTo(rectF, -90f, 90f)
      val rectF2 = RectF(middleRadius, 0f, middleRadius + gouRadius * 2, 0 + gouRadius * 2)
      gou.arcTo(rectF2, 0f, -90f)

      canvas.drawPath(gou, paint)
      canvas.restore()
    }

    // shader
    val shader = RadialGradient(0f, 0f, mRadius,
      intArrayOf(0x44000000, 0x00000000, 0x44000000), floatArrayOf(0f, 0.6f, 1f),
      Shader.TileMode.CLAMP)
    paint.shader = shader
    canvas.drawCircle(0f, 0f, middleRadius, paint)
    paint.shader = null
  }

}