package com.yuluyao.sharingan

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.math.tan

class Sa : Sharingan {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  private val matrix0 = Matrix()
  private val matrix1 = Matrix()
  private var kai = false

  init {
    matrix1.setRotate(60f)
    setOnClickListener {
      if (kai) {
        reset()
      } else {
        appearSasuke().start()
      }
      kai = !kai
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // 画6条弧线
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.03f
    drawArc(canvas)

    // 画黑色区域与圆点
    paint.color = primaryColor
    paint.style = Paint.Style.FILL
    val blackPath = obtainBlack(arcPath)
    canvas.drawPath(blackPath, paint)

    canvas.restore()
  }

  private fun drawArc(canvas: Canvas) {
    arcPath.reset()
    onePiece.reset()
    if (arcRatio == 0f) {
      return
    }
    canvas.save()
    canvas.translate(0f, -mRadius)
    canvas.rotate(45f)
    onePiece.moveTo(a.x, a.y)
    onePiece.cubicTo(c1.x, c1.y, c2.x, c2.y, b.x, b.y)
    canvas.restore()
    onePiece.transform(matrix0)

    for (i in 1..6) {
      arcPath.addPath(onePiece)
      onePiece.transform(matrix1)
    }
    canvas.drawPath(arcPath, paint)
  }

  private val arcPath = Path()
  private val onePiece = Path()


  private val a = PointF()
  private val b = PointF()
  private val c1 = PointF()
  private val c2 = PointF()

  private var outerRadius = 0F
  var arcRatio = 0f
    set(value) {
      field = value
      updatePoints()
      invalidate()
    }

  private fun updatePoints() {
    outerRadius = mRadius * sqrt(2.0f)

    matrix0.setTranslate(-outerRadius / 2f, outerRadius / 2f)

    a.x = outerRadius
    a.y = 0f
    b.x = 0f
    b.y = -outerRadius

    c1.x = outerRadius
    c1.y = -(4.0 * tan(PI / 8.0) / 3.0).toFloat() * outerRadius * arcRatio
    c2.x = (4.0 * tan(PI / 8.0) / 3.0).toFloat() * outerRadius * arcRatio
    c2.y = -outerRadius

  }


  var primaryColor = Color.TRANSPARENT
    set(value) {
      field = value
      invalidate()
    }

  private val blackArea = Path()
  private val circle = Path()
  private val dot = Path()
  private fun obtainBlack(dst: Path): Path {
    blackArea.reset()
    circle.reset()
    dot.reset()

    // 外圆,用来计算黑色区域
    circle.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
    // 中心的圆点
    dot.addCircle(0f, 0f, mRadius * 0.15f * arcRatio, Path.Direction.CCW)

    blackArea.op(dst, circle, Path.Op.XOR)
    blackArea.op(dot, Path.Op.XOR)
    return blackArea
  }


  private fun appearSasuke(): Animator {
    val a = ObjectAnimator.ofFloat(this, "arcRatio", 1f)
    a.duration = 500

    val b = ObjectAnimator.ofInt(this, "primaryColor", Color.BLACK)
    b.setEvaluator(ArgbEvaluator())
    b.duration = 100

    val appearSasukeSet = AnimatorSet()
    appearSasukeSet.playSequentially(b, a)

    val finalSet = AnimatorSet()
    finalSet.playSequentially(disappearSharingan(), appearSasukeSet)
    return finalSet
  }

  override fun reset() {
    super.reset()
    arcRatio = 0f
    primaryColor = Color.TRANSPARENT
    updatePoints()
    invalidate()
  }

}