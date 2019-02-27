package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import com.pyrapps.pyrtools.core.execution.Executor
import com.pyrapps.pyrtools.core.execution.async
import com.pyrapps.pyrtools.core.execution.sync

class CardsAdapter(
  private val executor: Executor,
  private val holdersCreator: (Int) -> ((ViewGroup) -> CardHolder),
  private val modelsDifferentiator: (Enum<*>, Any?, Any?) -> Boolean
) : Adapter<CardHolder>() {
  private val cards by lazy { mutableListOf<Card<*>>() }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ) =
    holdersCreator(viewType).invoke(parent)

  override fun onBindViewHolder(
    holder: CardHolder,
    position: Int
  ) {
    holder.fillViewWith(cards[position])
  }

  override fun getItemCount() = cards.size

  override fun getItemViewType(position: Int) = cards[position].type.ordinal

  fun update(newCards: List<Card<*>>) {
    val callback = CardsDiffCallback(cards, newCards, modelsDifferentiator)
    val f: () -> DiffUtil.DiffResult = { DiffUtil.calculateDiff(callback) }
    f.async(executor)
        .sync(executor) { diffResult ->
          cards.clear()
          cards.addAll(newCards)
          diffResult.dispatchUpdatesTo(this)
        }
  }

  infix fun getItemBy(id: String) = cards.find { it.id == id }

  fun notifyItemChanged(card: Card<*>) {
    val position = cards.indexOf(card)
    if (position > -1) notifyItemChanged(position)
  }
}