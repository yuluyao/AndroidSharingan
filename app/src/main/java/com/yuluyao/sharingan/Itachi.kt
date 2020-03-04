package com.yuluyao.sharingan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet


class Itachi : Eye {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  private val rotateMatrix = Matrix()

  init {
    rotateMatrix.setRotate(120f, 0f, 0f)
  }

  private val pA = PointF()
  private val pB = PointF()
  private val pC1 = PointF()
  private val pC2 = PointF()

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    // point A
    val innerRadius = mRadius * 0.3f
    pA.x = innerRadius
    pA.y = 0f
    // point B
    val degree_pb = 90
    val x_pb = (0 + mRadius * Math.cos(degree_pb * Math.PI / 180)).toFloat()
    val y_pb = (0 + mRadius * Math.sin(degree_pb * Math.PI / 180)).toFloat()
    pB.x = x_pb
    pB.y = y_pb
    // point C1
    val radius_c1 = mRadius * 0.85
    val x_c1 = innerRadius
    val y_c1 = Math.sqrt((radius_c1 * radius_c1 - innerRadius * innerRadius)).toFloat()
    pC1.x = x_c1
    pC1.y = y_c1
    // point C2
    val radiusC2 = mRadius * 0.4
    val x_c2 = innerRadius
    val y_c2 = -Math.sqrt((radiusC2 * radiusC2 - innerRadius * innerRadius)).toFloat()
    val array = floatArrayOf(x_c2, y_c2)
    rotateMatrix.mapPoints(array)
    pC2.x = array[0]
    pC2.y = array[1]
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(mWidth / 2, mHeight / 2)

    // middle ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.03f
    paint.color = 0x55000000.toInt()
    val middleRadius = mRadius * 0.6f
    canvas.drawCircle(0f, 0f, middleRadius, paint)

    // 画万花筒
    paint.style = Paint.Style.FILL
    paint.color= Color.BLACK
    val path = Path()
    path.moveTo(pA.x, pA.y)
    for (d in 0 until 360 step 120) {
      path.quadTo(pC1.x, pC1.y, pB.x, pB.y)
      rotatePoint(pA, rotateMatrix)
      path.quadTo(pC2.x, pC2.y, pA.x, pA.y)
      rotatePoint(pC1, rotateMatrix)
      rotatePoint(pB, rotateMatrix)
      rotatePoint(pC2, rotateMatrix)
    }
    path.addCircle(0f, 0f, mRadius * 0.125f, Path.Direction.CCW)
    path.fillType = Path.FillType.WINDING
    canvas.drawPath(path, paint)

    // test points
//    paint.style = Paint.Style.STROKE
//    paint.strokeWidth = 2 * density
//    paint.color = Color.GREEN
//    canvas.drawPoints(floatArrayOf(pA.x, pA.y, pB.x, pB.y, pC1.x, pC1.y, pC2.x, pC2.y), paint)

    canvas.restore()
  }

  private fun rotatePoint(point: PointF, matrix: Matrix) {
    val array = floatArrayOf(point.x, point.y)
    matrix.mapPoints(array)
    point.x = array[0]
    point.y = array[1]
  }

}