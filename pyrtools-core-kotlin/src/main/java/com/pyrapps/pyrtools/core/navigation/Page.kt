package com.pyrapps.pyrtools.core.navigation


interface Page<Params> {
    fun navigate(params: Params)
}