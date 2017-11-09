package com.pyrapps.pyrtools.gallery.usecase

import com.pyrapps.pyrtools.core.Result
import com.pyrapps.pyrtools.core.repository.Repository
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel


class LoadContentUseCase(private val repository: Repository<Unit, List<ImageModel>>)
    : (Unit?) -> Result<List<ImageModel>> {

    override fun invoke(ignore: Unit?) = repository.obtain()
}