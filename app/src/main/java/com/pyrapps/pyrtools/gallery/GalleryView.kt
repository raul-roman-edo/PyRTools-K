package com.pyrapps.pyrtools.gallery

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.pyrapps.pyrtools.R
import com.pyrapps.pyrtools.core.android.RawResourceByNameCommand
import com.pyrapps.pyrtools.core.android.repository.sources.PreferenceSource
import com.pyrapps.pyrtools.core.android.storage.PreferencesStore
import com.pyrapps.pyrtools.core.android.ui.cards.CardViewHolder
import com.pyrapps.pyrtools.core.android.ui.cards.CardsRecyclerView
import com.pyrapps.pyrtools.core.execution.AsyncExecutor
import com.pyrapps.pyrtools.core.repository.Repository
import com.pyrapps.pyrtools.core.storage.Store
import com.pyrapps.pyrtools.gallery.GalleryPresenter.View
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel
import com.pyrapps.pyrtools.gallery.usecase.ImagesSource
import com.pyrapps.pyrtools.gallery.usecase.LoadContentUseCase

class GalleryView @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) : CardsRecyclerView(context, attributeSet, defStyle), View {
  private lateinit var presenter: GalleryPresenter

  override fun initPresenter() {
    val repository = createRepository()
    val imagesLoader = LoadContentUseCase(repository)
    val asyncImagesLoader = AsyncExecutor()
    presenter = GalleryPresenter(this, imagesLoader, asyncImagesLoader)
  }

  override fun obtainLayoutManager() = GridLayoutManager(context, 2)

  override fun obtainHoldersCreator(): (Int) -> ((ViewGroup) -> CardViewHolder)? = { viewType ->
    when (viewType) {
      ImageModel.ID -> { viewGroup -> CardViewHolder(viewGroup, R.layout.card_image) }
      else -> null
    }
  }

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
    val prefsCache = PreferenceSource<Unit?, List<ImageModel>>(store)
    val resourceLoader = RawResourceByNameCommand(context.applicationContext)
    val remote = ImagesSource(resourceLoader)

    return Repository(listOf(prefsCache, remote))
  }
}