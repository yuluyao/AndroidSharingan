package com.yuluyao.sharingan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class Sharingan : Eye {


  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  private val rotateMatrix = Matrix()

  init {
    rotateMatrix.setRotate(120f, 0f, 0f)
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    canvas ?: return
    canvas.save()
    canvas.translate(mWidth / 2, mHeight / 2)

    // center dot
    paint.color = Color.BLACK
    val centerRadius = mRadius * 0.15f
    canvas.drawCircle(0f, 0f, centerRadius, paint)

    // middle ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.02f
    paint.color = 0x66000000.toInt()
    val middleRadius = mRadius * 0.6f
    canvas.drawCircle(0f, 0f, middleRadius, paint)


    // gou
    paint.color = Color.BLACK
    paint.style = Paint.Style.FILL
    val gouRadius = mRadius * 0.125f
    for (degree in -45 until 360 step 120) {
      canvas.save()
      canvas.rotate(degree.toFloat())

      // 画勾玉
      canvas.drawPath(obtainGouPath(middleRadius), paint)
      canvas.restore()
    }


    canvas.restore()
  }

  private val originalPath = Path()
  private fun obtainGouPath(middleRadius: Float): Path {
    if (originalPath.isEmpty) {
      val gouRadius = mRadius * 0.125f
      originalPath.addCircle(middleRadius, 0f, gouRadius, Path.Direction.CW)
      val rectF = RectF(middleRadius - gouRadius * 2, 0 - gouRadius,
        middleRadius + gouRadius * 2, 0 + gouRadius * 3)
      originalPath.arcTo(rectF, -90f, 90f)
      val rectF2 = RectF(middleRadius, 0f, middleRadius + gouRadius * 2, 0 + gouRadius * 2)
      originalPath.arcTo(rectF2, 0f, -90f)
    }

//    val tmpPath=Path()
//    originalPath.transform(rotateMatrix,tmpPath)
//    originalPath

    return originalPath
  }

}