package com.an.trailers.ui.base.custom.menu

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation


class FlipAnimation(
    private val mFromDegrees: Float, private val mToDegrees: Float,
    private val mCenterX: Float, private val mCenterY: Float
) : Animation() {
    private var mCamera: Camera? = null

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mCamera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val fromDegrees = mFromDegrees
        val degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        val centerX = mCenterX
        val centerY = mCenterY
        val camera = mCamera

        val matrix = t.matrix

        camera!!.save()

        camera.rotateY(degrees)

        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

    }

}