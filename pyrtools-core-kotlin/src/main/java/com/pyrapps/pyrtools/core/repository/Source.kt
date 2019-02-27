package com.pyrapps.pyrtools.core.repository

interface Source<Params, out Data> {
  fun request() = request(null)
  fun request(params: Params?): Pair<Params?, Data>
}