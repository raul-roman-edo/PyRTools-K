package com.pyrapps.pyrtools.gallery.usecase

import com.pyrapps.pyrtools.core.Result
import com.pyrapps.pyrtools.core.repository.Source
import com.pyrapps.pyrtools.core.repository.Updateable
import com.pyrapps.pyrtools.core.storage.Store
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel


class ImagesCache(private val store: Store<Unit, List<ImageModel>>)
    : Source<Unit, Result<List<ImageModel>>>, Updateable<Unit, Result<List<ImageModel>>> {

    override fun request(params: Unit?): Result<List<ImageModel>> {
        val images = store.load()
        return Result(!images.isEmpty(), images)
    }

    override fun update(params: Unit?, data: Result<List<ImageModel>>) {
        if (data.payload != null) store.save(data.payload!!)
    }
}