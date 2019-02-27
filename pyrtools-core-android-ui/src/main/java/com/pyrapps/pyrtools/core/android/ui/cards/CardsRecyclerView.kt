package com.pyrapps.pyrtools.core.android.ui.cards

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import com.pyrapps.pyrtools.core.execution.AsyncExecutor

abstract class CardsRecyclerView(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) : RecyclerView(context, attributeSet, defStyle) {
  init {
    initPresenter()
    initRecyclerView()
    launchEntriesLoading()
  }

  fun update(newCards: List<Card<*>>) {
    post { (adapter as CardsAdapter).update(newCards) }
  }

  protected abstract fun initPresenter()

  protected abstract fun obtainLayoutManager(): LayoutManager

  protected abstract fun obtainHoldersCreator(): (Int) -> (ViewGroup) -> CardHolder

  protected abstract fun obtainModelsDifferentiator(): (Enum<*>, Any?, Any?) -> Boolean

  protected abstract fun launchEntriesLoading()

  protected fun obtainItemDecoration(): ItemDecoration? = null

  protected fun createCardsAdapter() =
    CardsAdapter(AsyncExecutor(), obtainHoldersCreator(), obtainModelsDifferentiator())

  private fun initRecyclerView() {
    layoutManager = obtainLayoutManager()
    obtainItemDecoration()?.apply { addItemDecoration(this) }
    assignNewAdapter()
  }

  private fun assignNewAdapter() {
    adapter = createCardsAdapter()
  }
}


