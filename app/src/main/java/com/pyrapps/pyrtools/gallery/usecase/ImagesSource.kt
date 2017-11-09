package com.pyrapps.pyrtools.gallery.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pyrapps.pyrtools.core.Result
import com.pyrapps.pyrtools.core.android.RawResourceByNameCommand
import com.pyrapps.pyrtools.core.repository.Source
import com.pyrapps.pyrtools.gallery.cards.images.ImageModel


class ImagesSource(private val obtain: RawResourceByNameCommand)
    : Source<Unit, Result<List<ImageModel>>> {
    override fun request(params: Unit?): Result<List<ImageModel>> {
        val raw = obtain("images")
        if (raw.isEmpty()) return Result()
        return createResult(raw)
    }

    private fun createResult(raw: String): Result<List<ImageModel>> {
        val gson = Gson()
        val type = object : TypeToken<List<ImageModel>>() {}.type
        val cards = gson.fromJson<List<ImageModel>>(raw, type)
        return Result(true, cards)
    }
}