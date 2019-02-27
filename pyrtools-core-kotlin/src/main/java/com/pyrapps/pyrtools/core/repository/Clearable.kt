package com.pyrapps.pyrtools.core.repository

interface Clearable<Params> {
  fun clear() = clear(null)
  fun clear(params: Params?)
}