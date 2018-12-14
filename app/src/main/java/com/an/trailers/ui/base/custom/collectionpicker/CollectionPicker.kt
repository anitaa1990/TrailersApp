package com.an.trailers.ui.base.custom.collectionpicker

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.an.trailers.R

import java.util.*

class CollectionPicker
constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {

    private val genresList = Arrays.asList("#febf9b", "#f47f87", "#6ac68d", "#fbe0a5")

    private val mViewTreeObserver: ViewTreeObserver
    private val mInflater: LayoutInflater

    private var mItems: MutableList<String> = ArrayList()
    private var mRow: LinearLayout? = null

    private var mClickListener: OnItemClickListener? = null
    private var mWidth: Int = 0
    private var mItemMargin = 10
    private var textPaddingLeft = 5
    private var textPaddingRight = 5
    private var textPaddingTop = 5
    private var texPaddingBottom = 5
    private var mAddIcon = android.R.drawable.ic_menu_add
    private var mCancelIcon = android.R.drawable.ic_menu_close_clear_cancel
    private var mLayoutBackgroundColorNormal = R.color.blue
    private var mLayoutBackgroundColorPressed = R.color.red
    private var mTextColor = android.R.color.white
    private var mRadius = 5
    private var mInitialized: Boolean = false
    private val tf: Typeface?

    private val simplifiedTags: Boolean
    var isUseRandomColor: Boolean = false

    private val itemLayoutParams: LinearLayout.LayoutParams
        get() {
            val itemParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            itemParams.bottomMargin = mItemMargin / 2
            itemParams.topMargin = 0
            itemParams.rightMargin = mItemMargin

            return itemParams
        }


    private val selectorNormal: StateListDrawable
        @SuppressLint("ResourceAsColor")
        get() {
            val states = StateListDrawable()

            var gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(mLayoutBackgroundColorPressed)
            gradientDrawable.cornerRadius = mRadius.toFloat()

            states.addState(intArrayOf(android.R.attr.state_pressed), gradientDrawable)

            gradientDrawable = GradientDrawable()
            val index = Random().nextInt(genresList.size)
            if (isUseRandomColor) mLayoutBackgroundColorNormal = Color.parseColor(genresList[index])
            gradientDrawable.setColor(mLayoutBackgroundColorNormal)
            gradientDrawable.cornerRadius = mRadius.toFloat()

            states.addState(intArrayOf(), gradientDrawable)

            return states
        }


    private val selectorSelected: StateListDrawable
        @SuppressLint("ResourceAsColor")
        get() {
            val states = StateListDrawable()
            var gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(mLayoutBackgroundColorNormal)
            gradientDrawable.cornerRadius = mRadius.toFloat()

            states.addState(intArrayOf(android.R.attr.state_pressed), gradientDrawable)

            gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(mLayoutBackgroundColorPressed)
            gradientDrawable.cornerRadius = mRadius.toFloat()

            states.addState(intArrayOf(), gradientDrawable)

            return states
        }

    var items: MutableList<String>
        get() = mItems
        set(items) {
            mItems = items
            drawItemView()
        }

    private val isJellyBeanAndAbove: Boolean
        get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {}


    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CollectionPicker)
        this.mItemMargin = typeArray.getDimension(
            R.styleable.CollectionPicker_cp_itemMargin,
            dpToPx(this.context, mItemMargin).toFloat()
        ).toInt()
        this.textPaddingLeft = typeArray.getDimension(
            R.styleable.CollectionPicker_cp_textPaddingLeft,
            dpToPx(this.context, textPaddingLeft).toFloat()
        ).toInt()
        this.textPaddingRight = typeArray.getDimension(
            R.styleable.CollectionPicker_cp_textPaddingRight,
            dpToPx(this.context, textPaddingRight).toFloat()
        ).toInt()
        this.textPaddingTop = typeArray.getDimension(
            R.styleable.CollectionPicker_cp_textPaddingTop,
            dpToPx(this.context, textPaddingTop).toFloat()
        ).toInt()
        this.texPaddingBottom = typeArray.getDimension(
            R.styleable.CollectionPicker_cp_textPaddingBottom,
            dpToPx(this.context, texPaddingBottom).toFloat()
        ).toInt()
        this.mAddIcon = typeArray.getResourceId(R.styleable.CollectionPicker_cp_addIcon, mAddIcon)
        this.mCancelIcon = typeArray.getResourceId(R.styleable.CollectionPicker_cp_cancelIcon, mCancelIcon)
        this.mLayoutBackgroundColorNormal =
                typeArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundNormal, mLayoutBackgroundColorNormal)
        this.mLayoutBackgroundColorPressed =
                typeArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundPressed, mLayoutBackgroundColorPressed)
        this.mRadius = typeArray.getDimension(R.styleable.CollectionPicker_cp_itemRadius, mRadius.toFloat()).toInt()
        this.mTextColor = typeArray.getColor(R.styleable.CollectionPicker_cp_itemTextColor, mTextColor)
        this.simplifiedTags = typeArray.getBoolean(R.styleable.CollectionPicker_cp_simplified, false)
        this.tf = ResourcesCompat.getFont(getContext(), R.font.gt_medium)
        typeArray.recycle()

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.LEFT

        mViewTreeObserver = viewTreeObserver
        mViewTreeObserver.addOnGlobalLayoutListener {
            if (!mInitialized) {
                mInitialized = true
                drawItemView()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
    }

    fun drawItemView() {
        if (!mInitialized) {
            return
        }

        clearUi()

        var totalPadding = (paddingLeft + paddingRight).toFloat()
        var indexFrontView = 0

        val itemParams = itemLayoutParams

        for (i in mItems.indices) {
            val item = mItems[i]

            val position = i
            val itemLayout = createItemView(item)


            val itemTextView = itemLayout.findViewById<View>(R.id.item_text) as TextView
            itemTextView.isAllCaps = true
            itemTextView.typeface = tf
            itemTextView.textSize = 10f
            itemTextView.text = item
            itemTextView.setPadding(
                textPaddingLeft, textPaddingTop, textPaddingRight,
                texPaddingBottom
            )
            itemTextView.setTextColor(resources.getColor(mTextColor))

            var itemWidth = (itemTextView.paint.measureText(item) + textPaddingLeft.toFloat()
                    + textPaddingRight.toFloat())

            itemWidth += (dpToPx(context, 20) + textPaddingLeft
                    + textPaddingRight).toFloat()
            if (mWidth <= itemWidth + totalPadding) {
                totalPadding = (paddingLeft + paddingRight).toFloat()
                indexFrontView = i
                addItemView(itemLayout, itemParams, true, i)
            } else {
                if (i != indexFrontView) {
                    itemParams.rightMargin = mItemMargin
                    totalPadding += mItemMargin.toFloat()
                }
                addItemView(itemLayout, itemParams, false, i)
            }
            totalPadding += itemWidth
        }
        // }
    }

    private fun createItemView(s: String): View {
        val view = mInflater.inflate(R.layout.list_item_genre, this, false)
        if (isJellyBeanAndAbove) {
            view.background = getSelector(s)
        } else {
            view.setBackgroundDrawable(getSelector(s))
        }

        return view
    }

    private fun getItemIcon(isSelected: Boolean): Int {
        return if (isSelected) mCancelIcon else mAddIcon
    }

    private fun clearUi() {
        removeAllViews()
        mRow = null
    }

    private fun addItemView(
        itemView: View, chipParams: ViewGroup.LayoutParams, newLine: Boolean,
        position: Int
    ) {
        if (mRow == null || newLine) {
            mRow = LinearLayout(context)
            mRow!!.gravity = Gravity.LEFT
            mRow!!.orientation = LinearLayout.HORIZONTAL

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            mRow!!.layoutParams = params

            addView(mRow)
        }

        mRow!!.addView(itemView, chipParams)
        animateItemView(itemView, position)
    }

    private fun getSelector(s: String): StateListDrawable {
        return selectorNormal
    }

    fun setSelector(colorCode: Int) {
        this.mLayoutBackgroundColorNormal = colorCode
        selectorNormal
    }

    fun clearItems() {
        mItems.clear()
    }

    fun setTextColor(color: Int) {
        this.mTextColor = color
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mClickListener = clickListener
    }

    private fun animateView(view: View) {
        view.scaleY = 1f
        view.scaleX = 1f

        view.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(100)
            .setStartDelay(0)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    reverseAnimation(view)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            .start()
    }

    private fun reverseAnimation(view: View) {
        view.scaleY = 1.2f
        view.scaleX = 1.2f

        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(100)
            .setListener(null)
            .start()
    }

    private fun animateItemView(view: View, position: Int) {
        var animationDelay: Long = 600

        animationDelay += (position * 30).toLong()

        view.scaleY = 0f
        view.scaleX = 0f
        view.animate()
            .scaleY(1f)
            .scaleX(1f)
            .setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .setListener(null)
            .setStartDelay(animationDelay)
            .start()
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    interface OnItemClickListener {
        fun onClick(s: String, position: Int)
    }
}