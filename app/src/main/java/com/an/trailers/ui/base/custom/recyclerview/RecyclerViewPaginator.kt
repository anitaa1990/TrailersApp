package com.an.trailers.ui.base.custom.recyclerview

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE

abstract class RecyclerViewPaginator(private val recyclerView: RecyclerView) : RecyclerView.OnScrollListener() {

    private var currentPage: Long = 1L
    private val threshold = 2
    private var endWithAuto = false
    private lateinit var layoutManager: RecyclerView.LayoutManager

    abstract val isLastPage: Boolean

    init {
        init()
    }

    private fun init() {
        recyclerView.addOnScrollListener(this)
        this.layoutManager = recyclerView.layoutManager!!
        loadFirstData(currentPage)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager!!.itemCount

            var firstVisibleItemPosition = 0
            if (layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            } else if (layoutManager is GridLayoutManager) {
                firstVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }

            if (isLastPage) return

            if (visibleItemCount + firstVisibleItemPosition + threshold >= totalItemCount) {
                if (!endWithAuto) {
                    endWithAuto = true
                    loadMore(++currentPage)
                }
            } else {
                endWithAuto = false
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    abstract fun loadMore(page: Long)
    abstract fun loadFirstData(page: Long)
}