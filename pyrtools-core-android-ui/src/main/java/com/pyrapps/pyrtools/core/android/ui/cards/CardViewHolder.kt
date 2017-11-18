package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.pyrapps.pyrtools.core.android.ui.extensions.inflateView


class CardViewHolder(view: View?) : ViewHolder(view) {

    constructor(parent: ViewGroup, layoutId: Int) : this(parent.inflateView(layoutId))

    fun fillWith(card: Card<*>) {
        itemView.tag = card
    }
}