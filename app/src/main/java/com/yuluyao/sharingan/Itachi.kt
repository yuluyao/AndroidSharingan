package com.yuluyao.sharingan

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 鼬神的写轮眼
 */
class Itachi : Sharingan {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  private val rotateMatrix = Matrix()

  init {
    rotateMatrix.setRotate(120f, 0f, 0f)
    setOnClickListener { appearItachi().start() }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // itachi
    paint.style = Paint.Style.FILL
    paint.color = Color.BLACK
    drawItachi(canvas)

    canvas.restore()
  }

  private fun drawItachi(canvas: Canvas) {
    canvas.save()
    canvas.rotate(30f)
    canvas.drawPath(obtainItachiPath(), paint)
    canvas.restore()
  }


  var itachiRadius = 0F
    set(value) {
      field = value
      updatePoints()
      invalidate()
    }

  private val itachiPath = Path()
  private fun obtainItachiPath(): Path {
    itachiPath.reset()
    itachiPath.moveTo(pA.x, pA.y)
    for (d in 0 until 360 step 120) {
      itachiPath.quadTo(pC1.x, pC1.y, pB.x, pB.y)
      rotatePoint(pA, rotateMatrix)
      itachiPath.quadTo(pC2.x, pC2.y, pA.x, pA.y)
      rotatePoint(pC1, rotateMatrix)
      rotatePoint(pB, rotateMatrix)
      rotatePoint(pC2, rotateMatrix)
    }
    itachiPath.addCircle(0f, 0f, itachiRadius * 0.125f, Path.Direction.CCW)
    itachiPath.fillType = Path.FillType.WINDING
    return itachiPath
  }

  private val pA = PointF()
  private val pB = PointF()
  private val pC1 = PointF()
  private val pC2 = PointF()

  private fun updatePoints() {
    val innerRadius = itachiRadius * 0.3f
    // point A
    pA.x = innerRadius
    pA.y = 0f
    // point B
    val degree_pb = 90
    val x_pb = (0 + itachiRadius * cos(degree_pb * Math.PI / 180)).toFloat()
    val y_pb = (0 + itachiRadius * sin(degree_pb * Math.PI / 180)).toFloat()
    pB.x = x_pb
    pB.y = y_pb
    // point C1
    val radius_c1 = itachiRadius * 0.85
    val x_c1 = innerRadius
    val y_c1 = sqrt((radius_c1 * radius_c1 - innerRadius * innerRadius)).toFloat()
    pC1.x = x_c1
    pC1.y = y_c1
    // point C2
    val radiusC2 = itachiRadius * 0.4
    val x_c2 = innerRadius
    val y_c2 = -sqrt((radiusC2 * radiusC2 - innerRadius * innerRadius)).toFloat()
    val array = floatArrayOf(x_c2, y_c2)
    rotateMatrix.mapPoints(array)
    pC2.x = array[0]
    pC2.y = array[1]
  }

  private fun rotatePoint(point: PointF, matrix: Matrix) {
    val array = floatArrayOf(point.x, point.y)
    matrix.mapPoints(array)
    point.x = array[0]
    point.y = array[1]
  }

  private fun appearItachi(): Animator {
    val a = ObjectAnimator.ofFloat(this, "itachiRadius", mRadius)
    a.duration = 500
    a.interpolator = DecelerateInterpolator()

    val appearItachiSet = AnimatorSet()
    appearItachiSet.playTogether(a)

    val finalSet = AnimatorSet()
    finalSet.playSequentially(disappearSharingan(), appearItachiSet)
    return finalSet
  }

}