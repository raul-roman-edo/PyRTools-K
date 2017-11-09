package com.pyrapps.pyrtools.core.android

import android.content.Context
import com.pyrapps.pyrtools.core.android.extensions.obtainStringBy

class TextByKeyCommand(private val applicationContext: Context) : (String) -> String {
    override fun invoke(key: String) = applicationContext.obtainStringBy(key)
}