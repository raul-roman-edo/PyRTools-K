package com.pyrapps.pyrtools.core.android.extensions

import android.content.Context

fun Context.loadRawFileBy(name: String): String {
    val id = resources.getIdentifier(name, "raw", packageName)
    val fileStream = when (id) {
        0 -> null
        else -> resources.openRawResource(id)
    }
    return fileStream?.bufferedReader()?.use { it.readText() } ?: ""
}

fun Context.obtainStringBy(name: String): String {
    val id = resources.getIdentifier(name, "string", packageName)
    return if (id == 0) "" else getString(id)
}