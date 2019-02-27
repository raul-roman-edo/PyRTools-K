package com.pyrapps.pyrtools.core.storage

import java.lang.reflect.Type

class Store<Data> @JvmOverloads constructor (
  private val key: String,
  private val default: Data,
  private val storageSystem: StorageSystem<Data>,
  private val type: Type? = null
) {

  fun load() = storageSystem.load(key, default, type)

  fun save(data: Data?) {
    storageSystem.save(key, data, type)
  }

}