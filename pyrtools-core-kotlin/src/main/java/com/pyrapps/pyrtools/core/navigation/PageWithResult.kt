package com.pyrapps.pyrtools.core.navigation

interface PageWithResult<Result> {
  fun navigate(callback: (Result) -> Unit) = navigate(null, callback)
  fun <Params> navigate(params: Params?, callback: (Result) -> Unit)
}