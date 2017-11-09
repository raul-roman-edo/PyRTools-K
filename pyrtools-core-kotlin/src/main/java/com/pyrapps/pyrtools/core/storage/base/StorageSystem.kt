package com.pyrapps.pyrtools.core.storage.base


interface StorageSystem {
    fun load(key: String, default: String): String

    fun save(key: String, value: String)
}