package com.pyrapps.pyrtools.gallery

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.pyrapps.pyrtools.R
import com.pyrapps.pyrtools.core.android.RawResourceByNameCommand
import com.pyrapps.pyrtools.core.android.repository.sources.PreferenceSource
import com.pyrapps.pyrtools.core.android.storage.PreferencesStore
import com.pyrapps.pyrtools.core.android.ui.cards.CardHolder
import com.pyrapps.pyrtools.core.android.ui.cards.CardsRecyclerView
import com.pyrapps.pyrtools.core.execution.AsyncExecutor
import com.pyrapps.pyrtools.core.repository.Repository
import com.pyrapps.pyrtools.core.storage.Store
import com.pyrapps.pyrtools.gallery.GalleryPresenter.CardType.IMAGE
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel
import com.pyrapps.pyrtools.gallery.usecase.ImagesSource
import com.pyrapps.pyrtools.gallery.usecase.LoadContentUseCase

class GalleryView @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) : CardsRecyclerView(context, attributeSet, defStyle), GalleryPresenter.View {

  private lateinit var presenter: GalleryPresenter

  override fun initPresenter() {
    val repository = createRepository()
    val imagesLoader = LoadContentUseCase(repository)
    val asyncImagesLoader = AsyncExecutor()
    presenter = GalleryPresenter(this, imagesLoader, asyncImagesLoader)
  }

  override fun obtainLayoutManager() = GridLayoutManager(context, 2)

  override fun obtainHoldersCreator(): (Int) -> (ViewGroup) -> CardHolder = { viewType ->
    when (viewType) {
      IMAGE.ordinal -> { viewGroup -> CardHolder(viewGroup, R.layout.card_image) }
      else -> { _ -> CardHolder(View(context)) }
    }
  }

  override fun obtainModelsDifferentiator(): (Enum<*>, Any?, Any?) -> Boolean = { type, old, new ->
    when (type) {
      IMAGE -> compare(old as? ImageModel, new as? ImageModel)
      else -> false
    }
  }

  private fun compare(
    old: ImageModel?,
    new: ImageModel?
  ) = old == new

  override fun launchEntriesLoading() {
    presenter.onViewStarting()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    presenter.onViewFinishing()
  }

  private fun createRepository(): Repository<Unit?, List<ImageModel>> {
    val storageSystem = PreferencesStore<List<ImageModel>>(context.applicationContext)
    val store = Store(
        key = "images",
        default = listOf(),
        storageSystem = storageSystem,
        type = object : TypeToken<List<ImageModel>>() {}.type
    )
    val prefsCache =
      PreferenceSource<Unit?, List<ImageModel>>(store).apply { isValid = { it.isNotEmpty() } }
    val resourceLoader = RawResourceByNameCommand(context.applicationContext)
    val remote = ImagesSource(resourceLoader)

    return Repository(listOf(prefsCache, remote))
  }
}