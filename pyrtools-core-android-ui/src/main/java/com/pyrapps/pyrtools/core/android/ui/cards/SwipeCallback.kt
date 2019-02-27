package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeCallback(
  swipeDirs: Int,
  private val swipedFrom: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(NOT_MOVE_FLAG, swipeDirs) {
  companion object {
    val NOT_MOVE_FLAG = 0
  }

  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: ViewHolder,
    target: ViewHolder
  ) = false

  override fun onSwiped(
    viewHolder: ViewHolder,
    direction: Int
  ) {
    val position = viewHolder.layoutPosition
    swipedFrom(position)
  }

  override fun getSwipeDirs(
    recyclerView: RecyclerView,
    viewHolder: ViewHolder
  ): Int {
    val card = viewHolder.itemView.tag as Card<*>
    return if (card.isSwipeable) super.getSwipeDirs(recyclerView, viewHolder) else NOT_MOVE_FLAG
  }
}