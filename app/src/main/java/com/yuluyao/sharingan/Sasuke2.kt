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

class Sasuke2 : Sharingan {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    animateOnClick()
  }


  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    arcRadius = mRadius * sqrt(2.0f)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // 画6条弧线
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.03f
    drawComplexBezier(canvas)

    // 画黑色区域与圆点
    paint.color = primaryColor
    paint.style = Paint.Style.FILL
    val blackPath = obtainBlackPath(complexBezierPath)
    canvas.drawPath(blackPath, paint)

    canvas.restore()
  }

  //<editor-fold desc="交叉贝塞尔曲线">
  private fun drawComplexBezier(canvas: Canvas) {
    complexBezierPath.reset()
    bezierPath.reset()
    // ratio太小则不绘制交叉曲线
    if (bezierRatio < 0.05f) {
      return
    }
    canvas.save()
    canvas.translate(0f, -mRadius)
    canvas.rotate(45f)
    bezierPath.moveTo(a.x, a.y)
    bezierPath.cubicTo(c1.x, c1.y, c2.x, c2.y, b.x, b.y)
    canvas.restore()
    bezierPath.transform(translateBezierMatrix)

    for (i in 1..6) {
      complexBezierPath.addPath(bezierPath)
      bezierPath.transform(rotateMatrix)
    }
    canvas.drawPath(complexBezierPath, paint)
  }

  var bezierRatio = 0f
    set(value) {
      field = value
      updatePoints()
      invalidate()
    }

  private val rotateMatrix = Matrix().apply { setRotate(60f) } // 旋转60度


  private val complexBezierPath = Path()
  private val bezierPath = Path()

  private var arcRadius = 0F // 被模拟的圆弧的半径

  private val a = PointF()
  private val b = PointF()
  private val c1 = PointF()
  private val c2 = PointF()

  private val translateBezierMatrix = Matrix() // 将构造好的bezier曲线平移到以原点为中心

  private fun updatePoints() {

    translateBezierMatrix.setTranslate(-arcRadius / 2f, arcRadius / 2f)

    a.x = arcRadius
    a.y = 0f
    b.x = 0f
    b.y = -arcRadius

    c1.x = arcRadius
    c1.y = -(4.0 * tan(PI / 8.0) / 3.0).toFloat() * arcRadius * bezierRatio
    c2.x = (4.0 * tan(PI / 8.0) / 3.0).toFloat() * arcRadius * bezierRatio
    c2.y = -arcRadius

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
  private fun obtainBlackPath(dst: Path): Path {
    blackPath.reset()
    circle.reset()
    dot.reset()

    // 外圆,用来计算黑色区域
    circle.addCircle(0f, 0f, mRadius, Path.Direction.CCW)
    // 中心的圆点
    dot.addCircle(0f, 0f, mRadius * 0.15f * bezierRatio, Path.Direction.CCW)

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
    val a = ObjectAnimator.ofFloat(this, "bezierRatio", 1f)
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
    bezierRatio = 0f
    primaryColor = Color.TRANSPARENT
    updatePoints()
    invalidate()
  }
  //</editor-fold>

}
