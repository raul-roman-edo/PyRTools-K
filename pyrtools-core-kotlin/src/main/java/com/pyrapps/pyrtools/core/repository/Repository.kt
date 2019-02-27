package com.pyrapps.pyrtools.core.repository

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error

class Repository<Params, Data>(private val sources: List<Source<Params, Either<Error, Data>>>) {

  fun obtain(params: Params? = null): Pair<Params?, Either<Error, Data>> {
    var data: Pair<Params?, Either<Error, Data>> = Pair(params, Either.Left(Error("", "")))
    sources.find {
      data = it.request(params)
      data.second is Either.Right
    }
        ?.let { updateSources(params, (data.second as Either.Right).value, it) }
    return data
  }

  fun update(
    params: Params? = null,
    data: Data
  ) {
    sources.filter { it is Updateable<*, *> }
        .forEach { (it as Updateable<Params, Data>).update(params, data) }
  }

  fun clear(params: Params? = null) {
    sources.filter { it is Clearable<*> }
        .forEach { (it as Clearable<Params>).clear(params) }
  }

  private fun updateSources(
    params: Params?,
    result: Data,
    source: Source<Params, Either<Error, Data>>
  ) {
    val lastPositionToUpdate = sources.indexOfFirst { it == source }
    if (lastPositionToUpdate < 0) return
    sources.take(lastPositionToUpdate)
        .filter { it is Updateable<*, *> }
        .forEach { (it as Updateable<Params, Data>).update(params, result) }
  }
}