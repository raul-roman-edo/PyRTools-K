package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.util.DiffUtil

class CardsDiffCallback(
  private val oldList: List<Card<*>>,
  private val newList: List<Card<*>>,
  private val modelsDifferentiator: (Enum<*>, Any?, Any?) -> Boolean
) : DiffUtil.Callback() {
  override fun getOldListSize() = oldList.size

  override fun getNewListSize() = newList.size

  override fun areItemsTheSame(
    oldItemPosition: Int,
    newItemPosition: Int
  ) = oldList[oldItemPosition] == newList[newItemPosition]

  override fun areContentsTheSame(
    oldItemPosition: Int,
    newItemPosition: Int
  ): Boolean {
    val oldCard = oldList[oldItemPosition]
    val newCard = newList[newItemPosition]
    if (oldCard.type != oldCard.type) return false
    return modelsDifferentiator(oldCard.type, oldCard.model, newCard.model)
  }
}