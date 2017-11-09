package com.pyrapps.pyrtools.core.android.ui.cards

import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup


class CardsAdapter(private val holdersCreator: (Int) -> ((ViewGroup) -> CardViewHolder)?)
    : Adapter<CardViewHolder>() {
    companion object {
        private val INVALID_POSITION = -1
    }

    private val cards by lazy { mutableListOf<Card<*>>() }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = holdersCreator(viewType)?.invoke(parent!!)

    override fun onBindViewHolder(holder: CardViewHolder?, position: Int) {
        holder?.fillWith(cards[position])
    }

    override fun getItemCount() = cards.size

    override fun getItemViewType(position: Int) = cards[position].type

    fun obtainCardBy(position: Int) = cards[position]

    fun addTo(position: Int, card: Card<*>) {
        cards.add(position, card)
        notifyItemInserted(position)
    }

    fun addToBottom(card: Card<*>) {
        addTo(itemCount, card)
    }

    fun addToBottom(newCards: List<Card<*>>) {
        val current = itemCount
        cards.addAll(newCards)
        notifyItemRangeInserted(current, newCards.size)
    }

    fun remove(card: Card<*>) {
        val position = cards.indexOfFirst { it.id == card.id }
        if (position == INVALID_POSITION) return
        cards.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceFor(newCards: List<Card<*>>) {
        clear()
        addToBottom(newCards)
    }

    fun update(newCards: List<Card<*>>) {
        removeCardsNotPresentIn(newCards)
        reorderCommonCards(newCards)
        addNewCards(newCards)
    }

    private fun clear() {
        val current = itemCount
        cards.clear()
        notifyItemRangeRemoved(0, current)
    }

    private fun removeCardsNotPresentIn(newCards: List<Card<*>>) {
        val oldCards = cards
        oldCards.filter { isNotInList(it, newCards) }.forEach(this::remove)
    }

    private fun isNotInList(card: Card<*>, cards: List<Card<*>>)
            = cards.indexOfFirst { it.id == card.id } == INVALID_POSITION

    private fun reorderCommonCards(newCards: List<Card<*>>) {
        val common = newCards.filter { it in cards }
        val oldCards = cards
        oldCards.forEach {
            val newPosition = common.indexOf(it)
            val oldPosition = cards.indexOf(it)
            if (newPosition != oldPosition) {
                moveTo(newPosition, oldPosition, common[newPosition])
            } else {
                notifyItemChanged(oldPosition)
            }
        }
    }

    private fun moveTo(newPosition: Int, oldPosition: Int, card: Card<*>) {
        cards.removeAt(oldPosition)
        cards.add(newPosition, card)
        notifyItemMoved(oldPosition, newPosition)
        notifyItemChanged(newPosition)
    }

    private fun addNewCards(newCards: List<Card<*>>) {
        newCards.forEachIndexed { position, card -> if (card !in cards) addTo(position, card) }
    }
}