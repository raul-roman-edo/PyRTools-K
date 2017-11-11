package com.pyrapps.pyrtools.core.repository


interface Clearable<in Params> {
    fun clear(params: Params? = null)
}