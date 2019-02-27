package com.pyrapps.pyrtools.gallery.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error
import com.pyrapps.pyrtools.core.android.RawResourceByNameCommand
import com.pyrapps.pyrtools.core.repository.Source
import com.pyrapps.pyrtools.core.toLeft
import com.pyrapps.pyrtools.core.toRight
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel

class ImagesSource(private val obtain: RawResourceByNameCommand) : Source<Unit?, Either<Error, List<ImageModel>>> {
  override fun request(params: Unit?): Pair<Unit?, Either<Error, List<ImageModel>>> {
    val raw = obtain("images")
    return params to if (raw.isEmpty()) {
      Error("0", "No elements").toLeft()
    } else {
      createResult(raw).toRight()
    }
  }

  private fun createResult(raw: String): List<ImageModel> {
    val gson = Gson()
    val type = object : TypeToken<List<ImageModel>>() {}.type
    return gson.fromJson(raw, type)
  }
}