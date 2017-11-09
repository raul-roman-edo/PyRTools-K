package com.pyrapps.pyrtools.gallery

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.ViewGroup
import com.pyrapps.pyrtools.R
import com.pyrapps.pyrtools.core.android.RawResourceByNameCommand
import com.pyrapps.pyrtools.core.android.storage.base.PreferencesStorageSystem
import com.pyrapps.pyrtools.core.android.ui.cards.CardViewHolder
import com.pyrapps.pyrtools.core.android.ui.cards.CardsRecyclerView
import com.pyrapps.pyrtools.core.execution.AsyncAction
import com.pyrapps.pyrtools.core.execution.SimpleBackgroundAction
import com.pyrapps.pyrtools.core.repository.Repository
import com.pyrapps.pyrtools.core.storage.base.StorageController
import com.pyrapps.pyrtools.gallery.GalleryPresenter.View
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel
import com.pyrapps.pyrtools.gallery.usecase.ImagesCache
import com.pyrapps.pyrtools.gallery.usecase.ImagesSource
import com.pyrapps.pyrtools.gallery.usecase.LoadContentUseCase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI


class GalleryView(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0)
    : CardsRecyclerView(context, attributeSet, defStyle), View {
    private lateinit var presenter: GalleryPresenter

    constructor(context: Context, attributeSet: AttributeSet? = null)
            : this(context, attributeSet, 0)

    override fun initPresenter() {
        val repository = createRepository()
        val imagesLoader = LoadContentUseCase(repository)
        val bgAction = SimpleBackgroundAction(CommonPool, imagesLoader)
        val asyncImagesLoader = AsyncAction(UI, bgAction)
        presenter = GalleryPresenter(this, asyncImagesLoader)
    }

    override fun obtainLayoutManager() = GridLayoutManager(context, 2)

    override fun obtainHoldersCreator(): (Int) -> ((ViewGroup) -> CardViewHolder)? = { viewType ->
        when (viewType) {
            ImageModel.ID -> { viewGroup -> CardViewHolder(viewGroup, R.layout.card_image) }
            else -> null
        }
    }

    override fun launchEntriesLoading() {
        presenter.start()
    }

    private fun createRepository(): Repository<Unit, List<ImageModel>> {
        val storageSystem = PreferencesStorageSystem(context.applicationContext)
        val store = StorageController<Unit, List<ImageModel>>("images", default = listOf(),
                store = storageSystem)
        store.configureType<List<ImageModel>>()
        val prefsCache = ImagesCache(store)
        val resourceLoader = RawResourceByNameCommand(context.applicationContext)
        val remote = ImagesSource(resourceLoader)

        return Repository(listOf(prefsCache, remote))
    }
}