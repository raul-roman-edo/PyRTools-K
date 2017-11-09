package com.pyrapps.pyrtools.core.extensions

import java.io.File
import java.io.FileWriter
import java.io.IOException

fun File.write(data: String, append: Boolean = false): Boolean {
    var result = true
    try {
        val writer = FileWriter(this, append)
        writer.write(data)
        writer.close()
    } catch (e: IOException) {
        result = false
    }
    return result
}

fun File.recursiveLs(): List<FileStatus> {
    if (!exists() || !isDirectory) return listOf()
    return list(this)
}

private fun list(dir: File): List<FileStatus> = dir.listFiles().flatMap {
    val status = FileStatus(it.absolutePath)
    with(status) {
        isDir = it.isDirectory
        canRead = it.canRead()
        canWrite = it.canWrite()
        canExecute = it.canExecute()
        lastModified = it.lastModified()
        size = it.length()
        content = it.recursiveLs().toMutableList()
    }
    return listOf(status)
}