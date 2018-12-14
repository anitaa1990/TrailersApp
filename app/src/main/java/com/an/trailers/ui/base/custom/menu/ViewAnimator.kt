package com.an.trailers.ui.base.custom.menu

import android.os.Handler
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import com.an.trailers.R
import com.an.trailers.utils.AppUtils

import java.util.ArrayList


class ViewAnimator(
    private val appCompatActivity: AppCompatActivity,
    private val slideMenuItems: List<SlideMenuItem>,
    private val drawerLayout: DrawerLayout,
    private val animatorListener: ViewAnimatorListener
) {

    private val ANIMATION_DURATION = 175
    private val ASPECT_RATIO_WIDTH = 11.1
    private val ASPECT_RATIO_HEIGHT = 6.756
    private val ASPECT_RATIO_CONTAINER_HEIGHT = 15.9

    private val screenWidth: Int = AppUtils.getScreenWidth(appCompatActivity)
    private val screenHeight: Int = AppUtils.getScreenHeight(appCompatActivity)
    var selectedPosition = 0
        private set

    private val viewList = ArrayList<View>()

    private fun setViewsClickable(clickable: Boolean) {
        animatorListener.updateHomeButton(false)
        for (view in viewList) {
            view.isEnabled = clickable
        }
    }

    fun displayMenuContent() {
        setViewsClickable(false)
        viewList.clear()

        val size = slideMenuItems.size.toDouble()
        var i = 0
        while (i < size) {
            val viewMenu = appCompatActivity.layoutInflater.inflate(R.layout.list_item_menu, null)

            updateMenuItemImageView(i, viewMenu)
            updateMenuItemContainerView(i, viewMenu)

            viewList.add(viewMenu)
            animatorListener.addViewToContainer(viewMenu)
            if (i == selectedPosition) {
                viewList[i].isSelected = true
            }

            animateMenuItem(i.toDouble(), size)
            i++

        }
    }


    private fun updateMenuItemImageView(
        position: Int,
        viewMenu: View
    ) {
        val iv = viewMenu.findViewById<ImageView>(R.id.menu_item_image)

        val lp = iv.layoutParams
        val width = Math.ceil(ASPECT_RATIO_WIDTH * screenWidth / 100)
        val height = Math.ceil(ASPECT_RATIO_HEIGHT * screenHeight / 100)
        lp.width = width.toInt()
        lp.height = height.toInt()
        iv.layoutParams = lp

        iv.setImageResource(slideMenuItems[position].imageRes)
    }

    private fun updateMenuItemContainerView(
        position: Int,
        viewMenu: View
    ) {
        val container = viewMenu.findViewById<View>(R.id.menu_item_container)
        val layoutParams = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        container.layoutParams = layoutParams
        val containerHeight = Math.ceil(ASPECT_RATIO_CONTAINER_HEIGHT * screenHeight / 100)
        layoutParams.height = containerHeight.toInt()
        container.layoutParams = layoutParams

        viewMenu.visibility = View.GONE
        viewMenu.isEnabled = true

        viewMenu.setOnClickListener { v ->
            val location = intArrayOf(0, 0)
            v.getLocationOnScreen(location)
            switchItem(v, position, location[1] + v.height / 2)
        }
    }


    private fun animateMenuItem(
        position: Double,
        totalSize: Double
    ) {
        val delay = 3.0 * ANIMATION_DURATION.toDouble() * (position / totalSize)
        Handler().postDelayed({
            if (position < viewList.size) {
                animateView(position.toInt())
            }
        }, delay.toLong())
    }

    private fun hideMenuContent() {
        setViewsClickable(false)
        val size = slideMenuItems.size.toDouble()

        for (i in slideMenuItems.size downTo 0) {

            val position = i.toDouble()
            val delay = 3.0 * ANIMATION_DURATION.toDouble() * (position / size)

            Handler().postDelayed({
                if (position < viewList.size) {
                    animateHideView(position.toInt())
                }
            }, delay.toLong())
        }

    }

    private fun animateView(position: Int) {
        val view = viewList[position]
        view.visibility = View.VISIBLE
        val rotation = FlipAnimation(90f, 0f, 0.0f, view.height / 2.0f)
        rotation.duration = ANIMATION_DURATION.toLong()
        rotation.fillAfter = true
        rotation.interpolator = AccelerateInterpolator()
        rotation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        view.startAnimation(rotation)
    }


    private fun animateHideView(position: Int) {
        val view = viewList[position]
        val rotation = FlipAnimation(0f, 90f, 0.0f, view.height / 2.0f)
        rotation.duration = ANIMATION_DURATION.toLong()
        rotation.fillAfter = true
        rotation.interpolator = AccelerateInterpolator()
        rotation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.clearAnimation()
                view.visibility = View.INVISIBLE
                if (position == viewList.size - 1) {
                    animatorListener.updateHomeButton(true)
                    drawerLayout.closeDrawers()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        view.startAnimation(rotation)
    }


    private fun switchItem(view: View, selectedPosition: Int, topPosition: Int) {
        if (this.selectedPosition != selectedPosition && selectedPosition != viewList.size - 1) {
            view.isSelected = true
            updateSelectedView(view)
            this.selectedPosition = selectedPosition
            animatorListener.onSwitch(selectedPosition, topPosition)
        }
        hideMenuContent()
    }

    private fun updateSelectedView(view: View) {
        for (i in viewList.indices) {
            if (view.id != viewList[0].id) {
                viewList[i].isSelected = false
            }
        }
    }

    interface ViewAnimatorListener {
        fun onSwitch(selectedPosition: Int, topPosition: Int)
        fun updateHomeButton(enabled: Boolean)
        fun addViewToContainer(view: View)
    }
}