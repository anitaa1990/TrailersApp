package com.an.trailers.ui.base.custom.recyclerview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min


class PagerSnapHelper(private val recyclerSnapItemListener: RecyclerSnapItemListener) : LinearSnapHelper() {
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)

        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {

        return if (layoutManager is LinearLayoutManager) {

            if (layoutManager.canScrollHorizontally()) {
                getStartView(layoutManager, getHorizontalHelper(layoutManager))
            } else {
                getStartView(layoutManager, getVerticalHelper(layoutManager))
            }
        } else super.findSnapView(layoutManager)

    }


    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {

        val centerView = findSnapView(layoutManager!!) ?: return RecyclerView.NO_POSITION

        val position = layoutManager.getPosition(centerView)
        var targetPosition = -1
        if (layoutManager.canScrollHorizontally()) {
            targetPosition = if (velocityX < 0) {
                position - 1
            } else {
                position + 1
            }
        }

        if (layoutManager.canScrollVertically()) {
            targetPosition = if (velocityY < 0) {
                position - 1
            } else {
                position + 1
            }
        }

        val firstItem = 0
        val lastItem = layoutManager.itemCount - 1
        targetPosition = min(lastItem, max(targetPosition, firstItem))
        if (targetPosition >= 0) recyclerSnapItemListener.onItemSnap(targetPosition)
        return targetPosition
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    private fun getStartView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {

        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()

            val isLastItem = layoutManager
                .findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1

            if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }

            val child = layoutManager.findViewByPosition(firstChild)

            return if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(
                    child
                ) > 0
            ) {
                child
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    null
                } else {
                    layoutManager.findViewByPosition(firstChild + 1)
                }
            }
        }

        return super.findSnapView(layoutManager)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper!!
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }
}
