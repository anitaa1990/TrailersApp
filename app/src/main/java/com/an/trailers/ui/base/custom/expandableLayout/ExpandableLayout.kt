package com.an.trailers.ui.base.custom.expandableLayout

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.an.trailers.R

import com.an.trailers.ui.base.custom.expandableLayout.ExpandableLayout.State.*
import com.an.trailers.ui.base.custom.expandableLayout.ExpandableLayout.State.Companion.COLLAPSED
import com.an.trailers.ui.base.custom.expandableLayout.ExpandableLayout.State.Companion.COLLAPSING
import com.an.trailers.ui.base.custom.expandableLayout.ExpandableLayout.State.Companion.EXPANDED
import com.an.trailers.ui.base.custom.expandableLayout.ExpandableLayout.State.Companion.EXPANDING


class ExpandableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    var duration = DEFAULT_DURATION
    private var parallax: Float = 0.toFloat()
    private var expansion: Float = 0.toFloat()
    private var orientation: Int = 0

    var state: Int = 0
        private set

    private var interpolator: Interpolator = FastOutSlowInInterpolator()
    private var animator: ValueAnimator? = null

    private var listener: OnExpansionUpdateListener? = null

    var isExpanded: Boolean
        get() = state == EXPANDING || state == EXPANDED
        set(expand) = setExpanded(expand, true)

    interface State {
        companion object {
            val COLLAPSED = 0
            val COLLAPSING = 1
            val EXPANDING = 2
            val EXPANDED = 3
        }
    }

    init {

        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            duration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION)
            expansion = (if (a.getBoolean(R.styleable.ExpandableLayout_el_expanded, false)) 1 else 0).toFloat()
            orientation = a.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL)
            parallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1f)
            a.recycle()

            state = if (expansion == 0f) COLLAPSED else EXPANDED
            setParallax(parallax)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()

        expansion = (if (isExpanded) 1 else 0).toFloat()

        bundle.putFloat(KEY_EXPANSION, expansion)
        bundle.putParcelable(KEY_SUPER_STATE, superState)

        return bundle
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        state = if (expansion == 1f) EXPANDED else COLLAPSED
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)

        super.onRestoreInstanceState(superState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight

        val size = if (orientation == LinearLayout.HORIZONTAL) width else height

        visibility = if (expansion == 0f && size == 0) View.GONE else View.VISIBLE

        val expansionDelta = size - Math.round(size * expansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }

        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (animator != null) {
            animator!!.cancel()
        }
        super.onConfigurationChanged(newConfig)
    }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded) {
            return
        }

        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            setExpansion(targetExpansion.toFloat())
        }
    }

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun getExpansion(): Float {
        return expansion
    }

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) {
            return
        }

        // Infer state from previous value
        val delta = expansion - this.expansion
        if (expansion == 0f) {
            state = COLLAPSED
        } else if (expansion == 1f) {
            state = EXPANDED
        } else if (delta < 0) {
            state = COLLAPSING
        } else if (delta > 0) {
            state = EXPANDING
        }

        visibility = if (state == COLLAPSED) View.GONE else View.VISIBLE
        this.expansion = expansion
        requestLayout()

        if (listener != null) {
            listener!!.onExpansionUpdate(expansion, state)
        }
    }

    fun getParallax(): Float {
        return parallax
    }

    fun setParallax(parallax: Float) {
        var parallax = parallax
        // Make sure parallax is between 0 and 1
        parallax = Math.min(1f, Math.max(0f, parallax))
        this.parallax = parallax
    }

    fun getOrientation(): Int {
        return orientation
    }

    fun setOrientation(orientation: Int) {
        if (orientation < 0 || orientation > 1) {
            throw IllegalArgumentException("Orientation must be either 0 (horizontal) or 1 (vertical)")
        }
        this.orientation = orientation
    }

    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener) {
        this.listener = listener
    }

    private fun animateSize(targetExpansion: Int) {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion.toFloat())
        animator!!.interpolator = interpolator
        animator!!.duration = duration.toLong()

        animator!!.addUpdateListener { valueAnimator -> setExpansion(valueAnimator.animatedValue as Float) }

        animator!!.addListener(ExpansionListener(targetExpansion))

        animator!!.start()
    }

    interface OnExpansionUpdateListener {
        /**
         * Callback for expansion updates
         *
         * @param expansionFraction Value between 0 (collapsed) and 1 (expanded) representing the the expansion progress
         * @param state             One of [State] repesenting the current expansion state
         */
        fun onExpansionUpdate(expansionFraction: Float, state: Int)
    }

    private inner class ExpansionListener(private val targetExpansion: Int) : Animator.AnimatorListener {
        private var canceled: Boolean = false

        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) COLLAPSING else EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) COLLAPSED else EXPANDED
                setExpansion(targetExpansion.toFloat())
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    companion object {

        val KEY_SUPER_STATE = "super_state"
        val KEY_EXPANSION = "expansion"

        val HORIZONTAL = 0
        val VERTICAL = 1

        private val DEFAULT_DURATION = 300
    }
}