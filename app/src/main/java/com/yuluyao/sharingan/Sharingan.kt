package com.yuluyao.sharingan

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log

class Sharingan : Eye {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  init {
    setOnClickListener {
      disappearGou()
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    gouRadius = mRadius * 0.125f
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // center dot
    paint.color = Color.BLACK
    val centerRadius = mRadius * 0.15f
    canvas.drawCircle(0f, 0f, centerRadius, paint)

    // middle ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.03f
    paint.color = 0x55000000.toInt()
    val middleRadius = mRadius * 0.6f
    canvas.drawCircle(0f, 0f, middleRadius, paint)


    // gou
    paint.color = Color.BLACK
    paint.style = Paint.Style.FILL
    for (degree in 0 until 360 step 120) {
      canvas.save()
      canvas.rotate(degree.toFloat())

      // 画勾玉
      canvas.drawPath(buildGouPath(middleRadius), paint)
      canvas.restore()
    }


    canvas.restore()
  }


  var gouRadius = 0F
    set(value) {
      field = value
      invalidate()
    }

  private val gouPath = Path()
  private fun buildGouPath(middleRadius: Float): Path {
    gouPath.reset()
    gouPath.addCircle(middleRadius, 0f, gouRadius, Path.Direction.CW)
    val rectF = RectF(middleRadius - gouRadius * 2, 0 - gouRadius, middleRadius + gouRadius * 2, 0 + gouRadius * 3)
    gouPath.arcTo(rectF, -90f, 90f)
    val rectF2 = RectF(middleRadius, 0f, middleRadius + gouRadius * 2, 0 + gouRadius * 2)
    gouPath.arcTo(rectF2, 0f, -90f)
    return gouPath
  }

  protected fun disappearGou() {
    Log.i("vegeta", "animator start")
    val objectAnimator = ObjectAnimator.ofFloat(this, "gouRadius", 0f)
    objectAnimator.duration = 2000
    objectAnimator.start()
  }
}