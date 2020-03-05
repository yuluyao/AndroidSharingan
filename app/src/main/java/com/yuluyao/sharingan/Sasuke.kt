package com.yuluyao.sharingan

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import kotlin.math.sqrt

class Sasuke : Sharingan {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  private val rotateMatrix = Matrix()

  init {
    rotateMatrix.setRotate(60f)
    setOnClickListener { appearSasuke().start() }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // 画6条弧线
    paint.color = Color.BLACK
    paint.strokeWidth = mRadius * 0.03f
    paint.style = Paint.Style.STROKE
    val arc6Path = obtainSasukePath()
    canvas.drawPath(arc6Path, paint)

    // 画黑色区域与圆点
    paint.color = primaryColor
    paint.style = Paint.Style.FILL
    val blackPath = obtainBlack(arc6Path)
    canvas.drawPath(blackPath, paint)

    canvas.restore()
  }


  private var outerRadius = 0F
  private val outerRectF = RectF()

  private fun updateOuterRectF() {
    outerRadius = sqrt(2.0f) * sasukeRadius
    outerRectF.set(-outerRadius, sasukeRadius - outerRadius, outerRadius, sasukeRadius + outerRadius)
  }

  var sasukeRadius = 0F
    set(value) {
      field = value
      updateOuterRectF()
      invalidate()
    }

  private val sasukePath = Path()

  // 万花筒的6条弧线，其中1条
  private val arcPath = Path()

  private fun obtainSasukePath(): Path {
    sasukePath.reset()
    arcPath.reset()

    arcPath.moveTo(-sasukeRadius, 0f)
    arcPath.arcTo(outerRectF, -135f, 90f)

    var i = 0
    while (i++ < 6) {
      arcPath.transform(rotateMatrix)
      sasukePath.addPath(arcPath)
    }
    return sasukePath
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
    dot.addCircle(0f, 0f, sasukeRadius * 0.15f, Path.Direction.CCW)

    blackArea.op(dst, circle, Path.Op.XOR)
    blackArea.op(dot, Path.Op.XOR)
    return blackArea
  }


  private fun appearSasuke(): Animator {
    val a = ObjectAnimator.ofFloat(this, "sasukeRadius", mRadius)
    a.duration = 500
//    a.interpolator = DecelerateInterpolator()

    val b = ObjectAnimator.ofInt(this, "primaryColor", Color.BLACK)
    b.setEvaluator(ArgbEvaluator())
    b.duration = 100

    val appearSasukeSet = AnimatorSet()
    appearSasukeSet.playSequentially(b, a)

    val finalSet = AnimatorSet()
    finalSet.playSequentially(disappearSharingan(), appearSasukeSet)
    return finalSet
  }


}