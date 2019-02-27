package com.pyrapps.pyrtools.core.android.repository.sources

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error
import com.pyrapps.pyrtools.core.repository.FullSource
import com.pyrapps.pyrtools.core.storage.Store
import com.pyrapps.pyrtools.core.toLeft
import com.pyrapps.pyrtools.core.toRight

class PreferenceSource<Params, Data>(
  private val store: Store<Data>
) : FullSource<Params, Data> {
  var isValid: ((Data) -> Boolean)? = null

  override fun request(params: Params?): Pair<Params?, Either<Error, Data>> {
    val pref = store.load()
    val isValid = this.isValid?.invoke(pref) != false
    return params to if (!isValid) {
      Error("-1", "not found").toLeft()
    } else {
      pref.toRight()
    }
  }

  override fun update(
    params: Params?,
    data: Data
  ) {
    store.save(data)
  }

  override fun clear(params: Params?) {
    store.save(null)
  }
}