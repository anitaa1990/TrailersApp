package com.an.trailers.ui.base.custom.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerItemClickListener(
    context: Context,
    private val recyclerViewItemClickListener: OnRecyclerViewItemClickListener
) : RecyclerView.OnItemTouchListener {

    private val mGestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }
    })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null && recyclerViewItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            recyclerViewItemClickListener.onItemClick(view, childView, view.getChildLayoutPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {}

    interface OnRecyclerViewItemClickListener {
        fun onItemClick(parentView: View, childView: View, position: Int)
    }
}