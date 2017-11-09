package com.pyrapps.pyrtools.core.repository

interface Source<in Params, out Data> {
    fun request(params: Params? = null): Data
}