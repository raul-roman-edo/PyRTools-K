package com.pyrapps.pyrtools.core.storage

import java.lang.reflect.Type

interface StorageSystem<Data> {
  fun load(key: String, default: Data) = load(key, default, null)
  fun load(key: String, default: Data, type: Type?): Data

  fun save(key: String, value: Data?) = save(key, value, null)
  fun save(key: String, value: Data?, type: Type?)

  fun list(): MutableSet<String>

  fun remove(key: String)
}