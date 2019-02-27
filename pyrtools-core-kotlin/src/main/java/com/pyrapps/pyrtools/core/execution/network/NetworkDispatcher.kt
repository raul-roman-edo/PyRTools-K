package com.pyrapps.pyrtools.core.execution.network

import java.util.*

class NetworkDispatcher<Payload>(success: ((ApiResponse<Payload>) -> Unit)? = null,
                                 error: ((ApiResponse<Payload>) -> Unit)? = null,
                                 connectivityError: ((ApiResponse<Payload>) -> Unit)? = null)
    : (ApiResponse<Payload>) -> Unit {
    private var statusCallbacks: MutableMap<Int, (ApiResponse<Payload>) -> Unit> = mutableMapOf()
    private var errorCallbacks: MutableMap<String, (ApiResponse<Payload>) -> Unit> = mutableMapOf()
    private var globalCallbacks: NavigableMap<Int, (ApiResponse<Payload>) -> Unit> = TreeMap()

    init {
        globalCallbacks[100] = success
        globalCallbacks[400] = error
        globalCallbacks[0] = connectivityError
    }

    fun addStatusCallback(status: Int, callback: (ApiResponse<Payload>) -> Unit) {
        statusCallbacks[status] = callback
    }

    fun addErrorCallback(errorCode: String, callback: (ApiResponse<Payload>) -> Unit) {
        errorCallbacks[errorCode] = callback
    }

    override fun invoke(result: ApiResponse<Payload>) {
        val status = result.code
        val isError = status >= 400
        val dispatch = when {
            isError -> errorCallbacks[result.errorCode] ?: statusCallbacks[status]
            else -> statusCallbacks[status]
        }
        dispatch?.invoke(result) ?: globalCallbacks.floorEntry(status).value?.invoke(result)
    }
}