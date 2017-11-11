package com.pyrapps.pyrtools.core.repository

import com.pyrapps.pyrtools.core.Result

class Repository<in Params, Data>(private val sources: List<Source<Params, Result<Data>>>) {
    fun obtain(params: Params? = null): Result<Data> {
        var result = Result<Data>()
        val source = sources.find {
            result = it.request(params)
            result.isValid
        }
        source?.apply { updateSources(params, result, this) }

        return result
    }

    fun clear(params: Params? = null) {
        sources.filter { it is Clearable<*> }.forEach { (it as Clearable<Params>).clear(params) }
    }

    private fun updateSources(params: Params?,
                              result: Result<Data>,
                              source: Source<Params, Result<Data>>) {
        val lastPositionToUpdate = sources.indexOfFirst { it == source }
        if (lastPositionToUpdate < 0) return
        sources.take(lastPositionToUpdate)
                .filter { it is Updateable<*, *> }
                .forEach { (it as Updateable<Params, Result<Data>>).update(params, result) }
    }
}