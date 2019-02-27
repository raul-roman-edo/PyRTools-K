package com.pyrapps.pyrtools.gallery.usecase

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error
import com.pyrapps.pyrtools.core.repository.Repository
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel

class LoadContentUseCase(private val repository: Repository<Unit?, List<ImageModel>>) : (Unit?) -> Pair<Unit?, Either<Error, List<ImageModel>>> {

  override fun invoke(param: Unit?) = repository.obtain()
}