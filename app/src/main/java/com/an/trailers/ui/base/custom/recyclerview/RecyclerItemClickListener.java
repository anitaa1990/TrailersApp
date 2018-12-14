package com.an.trailers.ui.base.custom.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private OnRecyclerViewItemClickListener recyclerViewItemClickListener;

    public RecyclerItemClickListener(Context context, OnRecyclerViewItemClickListener listener) {
        recyclerViewItemClickListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && recyclerViewItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            recyclerViewItemClickListener.onItemClick(view, childView, view.getChildLayoutPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View parentView, View childView, int position);
    }
}