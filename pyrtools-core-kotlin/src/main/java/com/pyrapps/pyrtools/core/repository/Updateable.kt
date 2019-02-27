package com.pyrapps.pyrtools.core.repository

interface Updateable<Params, in Data> {
  fun update(data: Data) = update(null, data)
  fun update(params: Params?, data: Data)
}