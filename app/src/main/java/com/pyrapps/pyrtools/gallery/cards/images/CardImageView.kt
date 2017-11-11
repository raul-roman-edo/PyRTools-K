package com.pyrapps.pyrtools.gallery.cards.images

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.pyrapps.pyrtools.core.android.ui.cards.Card
import com.pyrapps.pyrtools.core.android.ui.extensions.load


class CardImageView(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0)
    : ImageView(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet? = null)
            : this(context, attributeSet, 0)

    override fun setTag(tag: Any?) {
        super.setTag(tag)
        tag?.run { (this as Card<ImageModel>).model?.run { load(url) } }
    }
}