package com.pyrapps.pyrtools.gallery

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error
import com.pyrapps.pyrtools.core.android.ui.cards.Card
import com.pyrapps.pyrtools.core.execution.Executor
import com.pyrapps.pyrtools.core.execution.async
import com.pyrapps.pyrtools.core.execution.sync
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel

class GalleryPresenter(
  private val view: View,
  private val imagesRequest: (Unit?) -> Pair<Unit?, Either<Error, List<ImageModel>>>,
  private val executor: Executor
) {
  fun onViewStarting() {
    imagesRequest.async(executor, null)
        .sync(executor) { result ->
          result.second.fold(
              { },
              { createImagesDispatcher(it) }
          )
        }
  }

  fun onViewFinishing() {
    executor.cancelAll()
  }

  private fun createImagesDispatcher(result: List<ImageModel>) {
    val cards = result.flatMap { transformToCard(it) }
    view.refresh(cards)
  }

  private fun transformToCard(image: ImageModel) = listOf(Card(image.url, ImageModel.ID, image))

  interface View {
    fun refresh(newCards: List<Card<*>>)
  }
}