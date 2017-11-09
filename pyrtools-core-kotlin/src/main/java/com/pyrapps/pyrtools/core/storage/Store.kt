package com.pyrapps.pyrtools.core.storage


interface Store<in Params, Result> {
    fun load(params: Params? = null): Result
    fun save(data: Result)
}