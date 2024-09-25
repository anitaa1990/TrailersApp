package com.an.trailers.ui.base.custom.loaders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.*
import android.widget.LinearLayout
import com.an.trailers.R

class NewtonCradleLoading : LinearLayout {

    private var cradleBallOne: CradleBall? = null
    private var cradleBallTwo: CradleBall? = null
    private var cradleBallThree: CradleBall? = null
    private var cradleBallFour: CradleBall? = null
    private var cradleBallFive: CradleBall? = null


    var isStart = false
        private set

    private lateinit var rotateLeftAnimation: RotateAnimation//cradleBallOne left to right
    private lateinit var rotateRightAnimation: RotateAnimation//cradleBallFive right to left
    private lateinit var shakeLeftAnimation: TranslateAnimation
    private lateinit var shakeRightAnimation: TranslateAnimation

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.newton_cradle_loading, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        cradleBallOne = findViewById(R.id.ball_one)
        cradleBallTwo = findViewById(R.id.ball_two)
        cradleBallThree = findViewById(R.id.ball_three)
        cradleBallFour = findViewById(R.id.ball_four)
        cradleBallFive = findViewById(R.id.ball_five)

        initAnim()
    }


    private fun initAnim() {
        rotateRightAnimation = RotateAnimation(
            0f,
            (-DEGREE).toFloat(),
            RotateAnimation.RELATIVE_TO_SELF,
            PIVOT_X,
            RotateAnimation.RELATIVE_TO_SELF,
            PIVOT_Y
        )
        rotateRightAnimation.repeatCount = 1
        rotateRightAnimation.repeatMode = Animation.REVERSE
        rotateRightAnimation.duration = DURATION.toLong()
        rotateRightAnimation.interpolator = LinearInterpolator()
        rotateRightAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (isStart)
                    startRightAnim()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        shakeLeftAnimation = TranslateAnimation(0f, SHAKE_DISTANCE.toFloat(), 0f, 0f)
        shakeLeftAnimation.duration = DURATION.toLong()
        shakeLeftAnimation.interpolator = CycleInterpolator(2f)

        rotateLeftAnimation = RotateAnimation(
            0f,
            DEGREE.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF,
            PIVOT_X,
            RotateAnimation.RELATIVE_TO_SELF,
            PIVOT_Y
        )
        rotateLeftAnimation.repeatCount = 1
        rotateLeftAnimation.repeatMode = Animation.REVERSE
        rotateLeftAnimation.duration = DURATION.toLong()
        rotateLeftAnimation.interpolator = LinearInterpolator()
        rotateLeftAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                if (isStart) {
                    cradleBallTwo!!.startAnimation(shakeLeftAnimation)
                    cradleBallThree!!.startAnimation(shakeLeftAnimation)
                    cradleBallFour!!.startAnimation(shakeLeftAnimation)

                    cradleBallFive!!.startAnimation(rotateRightAnimation)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })


        shakeRightAnimation = TranslateAnimation(0f, (-SHAKE_DISTANCE).toFloat(), 0f, 0f)
        shakeRightAnimation.duration = DURATION.toLong()
        shakeRightAnimation.interpolator = CycleInterpolator(2f)
        shakeRightAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (isStart)
                    startLeftAnim()
            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun startLeftAnim() {
        cradleBallOne!!.startAnimation(rotateLeftAnimation)
    }

    private fun startRightAnim() {
        cradleBallTwo!!.startAnimation(shakeRightAnimation)
        cradleBallThree!!.startAnimation(shakeRightAnimation)
        cradleBallFour!!.startAnimation(shakeRightAnimation)
    }

    fun start() {
        if (!isStart) {
            isStart = true
            startLeftAnim()
        }
    }

    fun stop() {
        isStart = false
        cradleBallOne!!.clearAnimation()
        cradleBallTwo!!.clearAnimation()
        cradleBallThree!!.clearAnimation()
        cradleBallFour!!.clearAnimation()
        cradleBallFive!!.clearAnimation()
    }

    fun setLoadingColor(color: Int) {
        cradleBallOne!!.setLoadingColor(color)
        cradleBallTwo!!.setLoadingColor(color)
        cradleBallThree!!.setLoadingColor(color)
        cradleBallFour!!.setLoadingColor(color)
        cradleBallFive!!.setLoadingColor(color)
    }

    companion object {

        private val DURATION = 400
        private val SHAKE_DISTANCE = 2
        private val PIVOT_X = 0.5f
        private val PIVOT_Y = -3f
        private val DEGREE = 30
    }
}