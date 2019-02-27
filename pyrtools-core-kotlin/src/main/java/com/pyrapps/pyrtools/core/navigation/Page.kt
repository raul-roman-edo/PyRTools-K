package com.pyrapps.pyrtools.core.navigation

interface Page<Params> {
  fun navigate() = navigate(null)
  fun navigate(params: Params?)
}