package com.pyrapps.pyrtools.core.extensions


data class FileStatus(val name: String) {
    var isDir = false
    var canRead = false
    var canWrite = false
    var canExecute = false
    var lastModified = 0L
    var size = 0L
    var content: MutableList<FileStatus> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }
}