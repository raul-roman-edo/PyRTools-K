package com.pyrapps.pyrtools.gallery

import com.pyrapps.pyrtools.core.Result
import com.pyrapps.pyrtools.core.android.ui.cards.Card
import com.pyrapps.pyrtools.core.execution.AsyncAction
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel


class GalleryPresenter(private val view: View,
                       private val imagesRequest: AsyncAction<Unit?, Result<List<ImageModel>>>) {
    fun start() {
        imagesRequest.execute {
            val cards = it.payload?.run { this.flatMap { transformToCard(it) } } ?: listOf()
            view.refresh(cards)
        }
    }

    private fun transformToCard(image: ImageModel) = listOf(Card(image.url, ImageModel.ID, image))

    interface View {
        fun refresh(newCards: List<Card<*>>)
    }
}