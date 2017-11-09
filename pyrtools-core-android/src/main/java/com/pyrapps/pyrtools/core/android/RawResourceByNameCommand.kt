package com.pyrapps.pyrtools.core.android

import android.content.Context
import com.pyrapps.pyrtools.core.android.extensions.loadRawFileBy


class RawResourceByNameCommand(private val applicationContext: Context) : (String) -> String {
    override fun invoke(name: String) = applicationContext.loadRawFileBy(name)
}