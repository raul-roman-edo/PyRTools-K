package com.pyrapps.pyrtools.core.repository

interface Updateable<in Params, in Data> {
    fun update(params: Params? = null, data: Data)
}