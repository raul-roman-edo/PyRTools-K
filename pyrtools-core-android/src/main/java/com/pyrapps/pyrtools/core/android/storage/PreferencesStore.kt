package com.pyrapps.pyrtools.core.android.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.pyrapps.pyrtools.core.storage.StorageSystem
import java.lang.reflect.Type

class PreferencesStore<Data : Any> @JvmOverloads constructor(
  applicationContext: Context,
  name: String? = null
) : StorageSystem<Data> {
  private val gson by lazy { Gson() }
  private val preferences by lazy {
    name?.let { applicationContext.getSharedPreferences(it, MODE_PRIVATE) }
        ?: PreferenceManager.getDefaultSharedPreferences(applicationContext)
  }

  override fun load(
    key: String,
    default: Data,
    type: Type?
  ): Data = when (default) {
    is String -> preferences.getString(key, default) as Data
    is Boolean -> preferences.getBoolean(key, default) as Data
    is Int -> preferences.getInt(key, default) as Data
    is Float -> preferences.getFloat(key, default) as Data
    is Long -> preferences.getLong(key, default) as Data
    else -> loadValueThroughGson(key, default, type)
  }

  override fun save(
    key: String,
    value: Data?,
    type: Type?
  ) {
    preferences.edit()
        .apply {
          when (value) {
            null -> remove(key)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> putValueThroughGson(key, value, this, type)
          }
        }
        .commit()
  }

  override fun list() = preferences.all.keys

  override fun remove(key: String) {
    preferences.edit()
        .remove(key)
        .commit()
  }

  private fun loadValueThroughGson(
    key: String,
    default: Data,
    type: Type?
  ): Data {
    val raw = preferences.getString(key, null)
    if (raw.isNullOrEmpty()) return default
    return type?.let { gson.fromJson<Data>(raw, type) }
        ?: gson.fromJson<Data>(raw, default::class.java)
  }

  private fun putValueThroughGson(
    key: String,
    value: Data,
    editor: Editor?,
    type: Type?
  ) {
    val raw = type?.let { gson.toJson(value, type) } ?: gson.toJson(value, value::class.java)
    editor?.putString(key, raw)
  }
}