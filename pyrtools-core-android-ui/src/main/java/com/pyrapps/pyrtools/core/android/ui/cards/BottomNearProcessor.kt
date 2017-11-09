package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener


class BottomNearProcessor(private val preloadThreshold: Int) : OnScrollListener() {
    private var bottomNearProcessed = true
    private var bottomIsNear: (() -> Unit)? = null

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!bottomNearProcessed || bottomIsNear == null) return

        when (recyclerView?.layoutManager) {
            is LinearLayoutManager -> checkListBottom(recyclerView)
            is GridLayoutManager -> checkGridBottom(recyclerView)
        }
    }

    private fun checkListBottom(recyclerView: RecyclerView) {
        val last = recyclerView.adapter.itemCount - 1
        val lastVisible =
                (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        checkBottom(last, lastVisible, preloadThreshold);
    }

    private fun checkGridBottom(recyclerView: RecyclerView) {
        val last = recyclerView.adapter.itemCount - 1
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val numColumns = layoutManager.spanCount
        checkBottom(last, lastVisible, preloadThreshold * numColumns)
    }

    private fun checkBottom(last: Int, lastVisible: Int, preloadThreshold: Int) {
        if (last < 0) return
        val isNearBottom = last - lastVisible < preloadThreshold
        if (isNearBottom) {
            bottomNearProcessed = false
            bottomIsNear?.invoke()
        }
    }
}