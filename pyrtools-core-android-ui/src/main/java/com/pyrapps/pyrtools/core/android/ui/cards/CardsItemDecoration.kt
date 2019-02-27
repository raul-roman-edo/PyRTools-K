package com.pyrapps.pyrtools.core.android.ui.cards

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View

class CardsItemDecoration : ItemDecoration() {

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: State
  ) {
    if (view is CardSpacingProvider) {
      val viewRect = view.getSpacing(parent.getChildLayoutPosition(view))
      outRect.apply {
        left = viewRect.left
        top = viewRect.top
        right = viewRect.right
        bottom = viewRect.bottom
      }
    }
  }
}