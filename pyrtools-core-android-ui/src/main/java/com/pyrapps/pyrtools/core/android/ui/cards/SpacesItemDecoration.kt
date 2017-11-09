package com.pyrapps.pyrtools.core.android.ui.cards

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.view.View


class SpacesItemDecoration(private val space: Int, private val applyToSides: Boolean = true,
                           private val applyToFirstAndLast: Boolean = true) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?,
                                state: RecyclerView.State?) {
        val position = parent!!.getChildLayoutPosition(view)
        val first = position == 0
        val last = position == parent.adapter.itemCount - 1

        if (first) {
            if (applyToFirstAndLast) {
                outRect?.top = space
            }
        } else {
            outRect?.top = space / 2
        }

        if (last) {
            if (applyToFirstAndLast) {
                outRect?.bottom = space
            }
        } else {
            outRect?.bottom = space / 2
        }

        if (applyToSides) {
            outRect?.left = space
            outRect?.right = space
        }
    }
}