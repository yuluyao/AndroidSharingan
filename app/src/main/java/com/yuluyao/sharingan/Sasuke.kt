package com.yuluyao.sharingan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class Sasuke : Eye {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // 画6条弧线
    paint.color = Color.BLACK
    paint.strokeWidth = mRadius * 0.03f
    paint.style = Paint.Style.STROKE
    val arc6Path = obtainArc6()
    canvas.drawPath(arc6Path, paint)

    // 画黑色区域与圆点
    paint.style = Paint.Style.FILL
    val blackPath = obtainBlack(arc6Path)
    canvas.drawPath(blackPath, paint)

    canvas.restore()
  }

  // 万花筒的6条弧线
  private val arc6 = Path()
  private fun obtainArc6(): Path {
    if (arc6.isEmpty) {
      val originalPath = Path()
      val r2 = (Math.sqrt(2.0) * mRadius).toFloat()
      val rectF = RectF(-r2, mRadius - r2, r2, mRadius + r2)
      originalPath.moveTo(-mRadius, 0f)
      originalPath.arcTo(rectF, -135f, 90f)

      val matrix = Matrix()
      val arcs = floatArrayOf(0f, 120f, 240f, 60f, 180f, 300f)
      for (arc in arcs) {
        matrix.setRotate(arc)
        val tmpPath = Path()
        originalPath.transform(matrix, tmpPath)
        arc6.addPath(tmpPath)
      }
    }
    return arc6
  }

  private val blackArea = Path()
  private fun obtainBlack(dst: Path): Path {
    if (blackArea.isEmpty) {
      // 外圆,用来计算黑色区域
      val circle = Path()
      circle.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
      // 中心的圆点
      val dot = Path()
      dot.addCircle(0f, 0f, mRadius * 0.15f, Path.Direction.CCW)

      blackArea.op(dst, circle, Path.Op.XOR)
      blackArea.op(dot, Path.Op.XOR)
    }
    return blackArea
  }


}