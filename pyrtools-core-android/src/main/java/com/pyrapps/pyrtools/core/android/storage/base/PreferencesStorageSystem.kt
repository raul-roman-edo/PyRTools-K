package com.pyrapps.pyrtools.core.android.storage.base

import android.content.Context
import android.preference.PreferenceManager
import com.pyrapps.pyrtools.core.storage.base.StorageSystem

class PreferencesStorageSystem(private val applicationContext: Context) : StorageSystem {
    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    override fun load(key: String, default: String) = preferences.getString(key, default)!!

    override fun save(key: String, value: String) {
        preferences.edit().putString(key, value).commit()
    }
}