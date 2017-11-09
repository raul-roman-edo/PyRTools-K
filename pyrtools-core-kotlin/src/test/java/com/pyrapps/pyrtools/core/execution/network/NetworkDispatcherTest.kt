package com.pyrapps.pyrtools.core.execution.network

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Test

class NetworkDispatcherTest {
    private val callbackToCheck = mock<(ApiResponse<String>) -> Unit>()

    @Test
    fun `Checks global success is called`() {
        val dispatch = NetworkDispatcher(callbackToCheck)

        dispatch(ApiResponse(200))

        verify(callbackToCheck).invoke(any())
    }

    @Test
    fun `Checks global success is not called`() {
        val dispatch = NetworkDispatcher(callbackToCheck)

        dispatch(ApiResponse(0))

        verifyZeroInteractions(callbackToCheck)
    }

    @Test
    fun `Checks global error is called`() {
        val dispatch = NetworkDispatcher({}, callbackToCheck)

        dispatch(ApiResponse(500))

        verify(callbackToCheck).invoke(any())
    }

    @Test
    fun `Checks connectivity error is called`() {
        val dispatch = NetworkDispatcher({}, {}, callbackToCheck)

        dispatch(ApiResponse(0))

        verify(callbackToCheck).invoke(any())
    }

    @Test
    fun `Checks connectivity error is not called`() {
        val dispatch = NetworkDispatcher({}, {}, callbackToCheck)

        dispatch(ApiResponse(200))

        verifyZeroInteractions(callbackToCheck)
    }

    @Test
    fun `Checks status callback is called`() {
        val dispatch = NetworkDispatcher<String>()
        dispatch.addStatusCallback(302, callbackToCheck)
        dispatch.addErrorCallback("10000", {})

        dispatch(ApiResponse(302))

        verify(callbackToCheck).invoke(any())
    }

    @Test
    fun `Checks status callback not called because of priority`() {
        val dispatch = NetworkDispatcher<String>()
        dispatch.addStatusCallback(500, callbackToCheck)
        dispatch.addErrorCallback("10000", {})

        dispatch(ApiResponse(500, "10000"))

        verifyZeroInteractions(callbackToCheck)
    }

    @Test
    fun `Checks error callback is called`() {
        val dispatch = NetworkDispatcher<String>()
        dispatch.addStatusCallback(302, {})
        dispatch.addErrorCallback("10000", callbackToCheck)

        dispatch(ApiResponse(500, "10000"))

        verify(callbackToCheck).invoke(any())
    }
}