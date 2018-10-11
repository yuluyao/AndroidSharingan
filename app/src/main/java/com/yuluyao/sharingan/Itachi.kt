package com.yuluyao.sharingan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet


class Itachi : Eye {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


  private val pInnerA = PointF()
  private val pInnerB = PointF()
  private val pOuterC = PointF()
  private val pCtrlA = PointF()
  private val pCtrlB = PointF()


  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    // inner A
    val innerRadius = mRadius * 0.2f
    pInnerA.x = innerRadius
    pInnerA.y = 0f
    // inner B
    val degreeInnerB = 120
    val xb = (0 + innerRadius * Math.cos(degreeInnerB * Math.PI / 180)).toFloat()
    val yb = (0 + innerRadius * Math.sin(degreeInnerB * Math.PI / 180)).toFloat()
    pInnerB.x = xb
    pInnerB.y = yb
    // outer C
    val degreeOuter = 90
    val xc = (0 + mRadius * Math.cos(degreeOuter * Math.PI / 180)).toFloat()
    val yc = (0 + mRadius * Math.sin(degreeOuter * Math.PI / 180)).toFloat()
    pOuterC.x = xc
    pOuterC.y = yc
    // control A
    val degreeCA = 60
    val xca = (0 + mRadius * 0.8 * Math.cos(degreeCA * Math.PI / 180)).toFloat()
    val yca = (0 + mRadius * 0.8 * Math.sin(degreeCA * Math.PI / 180)).toFloat()
    pCtrlA.x = xca
    pCtrlA.y = yca
    // control B
    val degreeCB = 80
    val xcb = (0 + mRadius * 0.7 * Math.cos(degreeCB * Math.PI / 180)).toFloat()
    val ycb = (0 + mRadius * 0.7 * Math.sin(degreeCB * Math.PI / 180)).toFloat()
    pCtrlB.x = xcb
    pCtrlB.y = ycb

  }


  override fun onDraw(canvas: Canvas?) {
    canvas ?: return
    canvas.translate(mWidth / 2, mHeight / 2)

    // red background
    paint.style = Paint.Style.FILL
    paint.color = 0xffac0003.toInt()
    canvas.drawCircle(0f, 0f, mRadius, paint)

    // out ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.06f
    paint.color = 0xff000000.toInt()
    val outRadius = mRadius * 0.97f
    canvas.drawCircle(0f, 0f, outRadius, paint)

    // yu
//    paint.style = Paint.Style.FILL
//    paint.color = 0xff000000.toInt()
//    val path = Path()
//    path.moveTo(0f, 0f)
//    path.quadTo(60f, 60f, mRadius, 0f)
//    canvas.drawPath(path, paint)


    // 画花
    paint.style = Paint.Style.FILL
    paint.color = Color.BLACK
    val path = Path()
    path.moveTo(pInnerA.x, pInnerA.y)
    path.quadTo(pCtrlA.x, pCtrlA.y, pOuterC.x, pOuterC.y)
    path.quadTo(pCtrlB.x, pCtrlB.y, pInnerB.x, pInnerB.y)
    canvas.drawPath(path, paint)


    // test points
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 2 * density
    paint.color = Color.GREEN
    canvas.drawPoints(floatArrayOf(pInnerA.x, pInnerA.y, pInnerB.x, pInnerB.y, pOuterC.x, pOuterC.y,
      pCtrlA.x, pCtrlA.y, pCtrlB.x, pCtrlB.y), paint)


  }

}