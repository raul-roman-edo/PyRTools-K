package com.pyrapps.pyrtools.gallery.cards.images

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.pyrapps.pyrtools.core.android.ui.cards.Card
import com.pyrapps.pyrtools.core.android.ui.cards.FillableCardView
import com.pyrapps.pyrtools.core.android.ui.extensions.load

class CardImageView(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) : ImageView(context, attributeSet, defStyle), FillableCardView {
  constructor(
    context: Context,
    attributeSet: AttributeSet? = null
  )
      : this(context, attributeSet, 0)

  override fun fillWith(card: Card<*>?) {
    card?.model?.let { load((it as ImageModel).url) }
  }
}