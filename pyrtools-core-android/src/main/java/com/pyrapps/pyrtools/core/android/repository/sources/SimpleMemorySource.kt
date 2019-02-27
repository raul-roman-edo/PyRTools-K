package com.pyrapps.pyrtools.core.android.repository.sources

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error
import com.pyrapps.pyrtools.core.repository.Clearable
import com.pyrapps.pyrtools.core.repository.Source
import com.pyrapps.pyrtools.core.repository.Updateable

class SimpleMemorySource<Params, Data>
  : Source<Params, Either<Error, Data>>, Updateable<Params, Data>, Clearable<Params> {
  var isValid: ((Data) -> Boolean)? = null
  private val store = mutableMapOf<Int, Data>()

  override fun request(params: Params?): Pair<Params?, Either<Error, Data>> {
    val value = store[obtainKeyFrom(params)]
    val isValid = value != null && this.isValid?.invoke(value) != false
    val result = if (!isValid) {
      Either.Left(Error("-1", "not found"))
    } else {
      Either.Right(value!!)
    }
    return Pair(params, result)
  }

  override fun update(
    params: Params?,
    data: Data
  ) {
    store[obtainKeyFrom(params)] = data
  }

  override fun clear(params: Params?) {
    store.clear()
  }

  private fun obtainKeyFrom(params: Params?) = params?.hashCode() ?: 0
}