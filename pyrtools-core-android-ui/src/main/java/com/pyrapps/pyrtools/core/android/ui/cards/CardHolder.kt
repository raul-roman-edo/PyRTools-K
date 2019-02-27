package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.pyrapps.pyrtools.core.android.ui.extensions.inflateView

open class CardHolder(view: View, initializer: ((View) -> Unit)? = null) : ViewHolder(view) {

  constructor(
    parent: ViewGroup,
    layoutId: Int,
    initializer: ((View) -> Unit)? = null
  ) : this(parent.inflateView(layoutId), initializer)

  init {
    initializer?.apply { itemView.also(this) }
  }

  open fun fillViewWith(card: Card<*>?) {
    if (itemView !is FillableCardView) return
    itemView.fillWith(card)
  }
}
