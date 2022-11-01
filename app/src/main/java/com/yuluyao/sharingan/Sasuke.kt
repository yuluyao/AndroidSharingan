package com.yuluyao.sharingan

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import kotlin.math.sqrt

class Sasuke : Sharingan {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    animateOnClick()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // 画6条弧线
    paint.color = Color.BLACK
    paint.strokeWidth = mRadius * 0.03f
    paint.style = Paint.Style.STROKE
    val complexArcPath = obtainComplexArcPath()
    canvas.drawPath(complexArcPath, paint)

    // 画黑色区域与圆点
    paint.color = primaryColor
    paint.style = Paint.Style.FILL
    val blackPath = obtainBlack(complexArcPath)
    canvas.drawPath(blackPath, paint)

    canvas.restore()
  }

  //<editor-fold desc="交叉圆弧">
  var sasukeRadius = 0F
    set(value) {
      field = value
      updateOuterRectF()
      invalidate()
    }

  private var arcRadius = 0F
  private val arcRectF = RectF()

  private fun updateOuterRectF() {
    arcRadius = sqrt(2.0f) * sasukeRadius
    arcRectF.set(-arcRadius, sasukeRadius - arcRadius, arcRadius, sasukeRadius + arcRadius)
  }

  private val complexArcPath = Path()
  private val arcPath = Path() // 万花筒的6条弧线，其中1条
  private val rotateMatrix = Matrix().apply { setRotate(60f) } //旋转60度
  private fun obtainComplexArcPath(): Path {
    complexArcPath.reset()
    arcPath.reset()

    arcPath.moveTo(-sasukeRadius, 0f)
    arcPath.arcTo(arcRectF, -135f, 90f)

    var i = 0
    while (i++ < 6) {
      arcPath.transform(rotateMatrix)
      complexArcPath.addPath(arcPath)
    }
    return complexArcPath
  }
  //</editor-fold>

  //<editor-fold desc="黑色区域和黑色中心">
  var primaryColor = Color.TRANSPARENT
    set(value) {
      field = value
      invalidate()
    }

  private val blackPath = Path()
  private val circle = Path()
  private val dot = Path()
  private fun obtainBlack(dst: Path): Path {
    blackPath.reset()
    circle.reset()
    dot.reset()

    // 外圆,用来计算黑色区域
    circle.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
    // 中心的圆点
    dot.addCircle(0f, 0f, sasukeRadius * 0.15f, Path.Direction.CCW)

    blackPath.op(dst, circle, Path.Op.XOR)
    blackPath.op(dot, Path.Op.XOR)
    return blackPath
  }
  //</editor-fold>

  //<editor-fold desc="动画">
  private var kai = false
  private fun animateOnClick() {
    setOnClickListener {
      if (kai) {
        reset()
      } else {
        appearSasuke().start()
      }
      kai = !kai
    }
  }

  private fun appearSasuke(): Animator {
    val a = ObjectAnimator.ofFloat(this, "sasukeRadius", mRadius)
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
    sasukeRadius = 0f
    primaryColor = Color.TRANSPARENT
    updateOuterRectF()
    invalidate()
  }
  //</editor-fold>


}
