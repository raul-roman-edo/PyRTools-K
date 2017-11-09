package com.pyrapps.pyrtools.core.storage.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pyrapps.pyrtools.core.storage.Store
import java.lang.reflect.Type

class StorageController<in Param, Result>(private val key: String,
                                          private val resultClass: Class<Result>? = null,
                                          private val default: Result,
                                          private val store: StorageSystem) : Store<Param, Result> {
    var typeToken: Type? = null
    var gson = Gson()

    override fun load(params: Param?): Result {
        val raw = store.load(key, obtainRawContent(default))
        return createLoadedObject(raw)
    }

    override fun save(data: Result) {
        val raw = obtainRawContent(data)
        store.save(key, raw)
    }

    inline fun <reified Result> configureType() {
        typeToken = object : TypeToken<Result>() {}.type
    }

    private fun createLoadedObject(raw: String)
            = typeToken?.run { gson.fromJson<Result>(raw, this) }
            ?: gson.fromJson<Result>(raw, resultClass)

    private fun obtainRawContent(data: Result)
            = typeToken?.run { gson.toJson(data, this) }
            ?: gson.toJson(data, resultClass)
}